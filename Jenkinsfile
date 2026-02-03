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
                    // Publish test results
                    publishTestResults testResultsPattern: 'build/test-results/test/*.xml'
                    // Publish JaCoCo coverage report
                    publishCoverage adapters: [jacocoAdapter('build/reports/jacoco/test/jacocoTestReport.xml')], 
                                   sourceFileResolver: sourceFiles('STORE_LAST_BUILD')
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
