#!/usr/bin/env groovy

pipeline {
    agent any

    post {
        success {
            ircSendSuccess()
        }

        failure {
            ircSendFailure()
        }
    }

    stages {
        stage('Checkout') {
            steps {
                ircSendStarted()

                checkout scm
                sh "rm -Rv build || true"
            }
        }

        stage('Build') {
            steps {
                sh "./gradlew clean build -PBUILD_NUMBER=${env.BUILD_NUMBER} --no-daemon"
            }
        }

        stage('Coverage') {
            steps {
                sh "./gradlew jacocoTestReport --no-daemon"

                withCredentials([[$class: 'StringBinding', credentialsId: 'engineer.carrot.warren.kale.codecov', variable: 'CODECOV_TOKEN']]) {
                    sh "./codecov.sh -B ${env.BRANCH_NAME}"
                }

                step([$class: 'JacocoPublisher'])
            }
        }

        stage('Archive') {
            steps {
                archive includes: 'build/libs/*.jar'
                junit 'build/test-results/**/*.xml'
            }
        }

        stage('Deploy') {
            steps {
                sh "./gradlew publishMavenJavaPublicationToMavenRepository -PBUILD_NUMBER=${env.BUILD_NUMBER} -PDEPLOY_DIR=/var/www/maven.hopper.bunnies.io --no-daemon"
            }
        }
    }
}