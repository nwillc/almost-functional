pipeline {
 agent { docker 'gradle:3.2.1' }
    stages {
        stage('build') {
            steps {
                sh 'gradlew clean build'
            }
        }
    }
}
