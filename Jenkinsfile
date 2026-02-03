pipeline {
    agent any
    
    tools {
        gradle 'Gradle'
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
                    // Publish test results using junit step
                    junit testResultsPattern: 'build/test-results/test/*.xml', allowEmptyResults: true
                    
                    // Archive JaCoCo coverage reports
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
                // Archive the JAR file
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
