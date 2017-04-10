pipeline {
	agent any
    stages {
        stage('build') {
            steps {
                sh './gradlew clean compileJava'
            }
        }
	stage('test') {
		steps {
			sh './gradlew test'
		}
	}
    }
}
