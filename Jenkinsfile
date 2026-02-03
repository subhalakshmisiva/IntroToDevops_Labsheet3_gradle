pipeline {
    agent any
    
    tools {
        gradle 'Gradle'
    }
    
    environment {
        SONAR_TOKEN = credentials('sonar-token')
        SONAR_SCANNER_HOME = tool 'SonarQube Scanner'
        PATH = "${env.SONAR_SCANNER_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                echo 'Building and testing with Gradle...'
                script {
                    if (isUnix()) {
                        sh 'gradle clean test jacocoTestReport'
                    } else {
                        bat 'gradle clean test jacocoTestReport'
                    }
                }
            }
            post {
                always {
                    // Publish test results
                    junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
                    
                    // Publish JaCoCo coverage report
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'build/reports/jacoco/test/html',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    script {
                        if (isUnix()) {
                            sh '''
                                sonar-scanner \
                                -Dsonar.host.url=${SONAR_HOST_URL} \
                                -Dsonar.login=${SONAR_AUTH_TOKEN}
                            '''
                        } else {
                            bat '''
                                sonar-scanner ^
                                -Dsonar.host.url=%SONAR_HOST_URL% ^
                                -Dsonar.login=%SONAR_AUTH_TOKEN%
                            '''
                        }
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }
        
        stage('Archive Artifact') {
            steps {
                echo 'Creating and archiving JAR file...'
                script {
                    if (isUnix()) {
                        sh 'gradle jar'
                    } else {
                        bat 'gradle jar'
                    }
                }
                archiveArtifacts artifacts: 'build/libs/*.jar', 
                               fingerprint: true,
                               allowEmptyArchive: false
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed!'
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
