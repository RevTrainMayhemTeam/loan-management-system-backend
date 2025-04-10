/*
   Clones both frontend and backend
   Builds the React app
   Copies the frontend into the Spring Boot static folder
   Since I gitignore application.properties. I need to inject application.properties from Jenkins credentials as a secret file.
   Builds the Spring Boot JAR
   Copies everything needed (including Dockerfile and backend code) to the EC2 instance
   Remotely builds the Docker image and runs the container
*/
pipeline {
    agent any

    environment {
        IMAGE_NAME = "loan-management-system"
        CONTAINER_NAME = "lms-container"
        EC2_HOST = "ec2-user@54.144.224.97"
    }

    stages {
        stage('Clone Frontend Repo') {
            steps {
                dir('frontend') {
                    git url: 'https://github.com/RevTrainMayhemTeam/loan-management-system-frontend.git', branch: 'main'
                }
            }
        }

        stage('Clone Backend Repo') {
            steps {
                dir('backend') {
                    git url: 'https://github.com/RevTrainMayhemTeam/loan-management-system-backend.git', branch: 'main'
                }
            }
        }

        stage('Build React App') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Copy Frontend into Spring Static Folder') {
            steps {
                sh 'rm -rf backend/src/main/resources/static/*'
                sh 'cp -r frontend/dist/* backend/src/main/resources/static/'
            }
        }

        stage('Build Spring Boot JAR') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Copy Project to EC2') {
            steps {
                withCredentials([file(credentialsId: 'lms-ssh-keys', variable: 'EC2_KEY')]) {
                    sh '''
                        chmod 400 $EC2_KEY
                        ssh -o StrictHostKeyChecking=no -i $EC2_KEY ec2-user@54.144.224.97 "mkdir -p loan-management-system"
                        scp -o StrictHostKeyChecking=no -i $EC2_KEY backend/Dockerfile ec2-user@54.144.224.97:/home/ec2-user/loan-management-system/
                        scp -o StrictHostKeyChecking=no -i $EC2_KEY backend/target/loan-management-system-0.0.1-SNAPSHOT.jar ec2-user@54.144.224.97:/home/ec2-user/loan-management-system/
                    '''
                }
            }
        }

        stage('Deploy Docker Container on EC2') {
          steps {
              withCredentials([file(credentialsId: 'lms-ssh-keys', variable: 'EC2_KEY')]) {
                  sh """
                      # Stop and remove any existing container (ignore errors if it doesn't exist)
                      ssh -o StrictHostKeyChecking=no -i $EC2_KEY ec2-user@54.144.224.97 "
                          docker stop lms-container || true &&
                          docker rm lms-container || true
                      "
      
                      # Build and run the new container
                      ssh -o StrictHostKeyChecking=no -i $EC2_KEY ec2-user@54.144.224.97 "
                          cd /home/ec2-user/loan-management-system &&
                          docker build -t loan-management-system . &&
                          docker run -d --name lms-container -p 8080:8080 loan-management-system
                      "
                  """
              }
          }
      }

        stage('Done') {
            steps {
                echo "✅ App deployed successfully at http://54.144.224.97:8080"
            }
        }
    }
}
