pipeline {
    agent any

    tools {
        // Aquí usamos los nombres que configuraste en "Tools"
        jdk 'JDK21'
        gradle 'Gradle_Latest'
    }

    stages {
        stage('Explorar') {
            steps {
                // Un paso para verificar que todo esté en orden
                sh 'java -version'
                sh './gradlew --version'
            }
        }
    }

    steps {
        withCredentials([usernamePassword(credentialsId: 'MY_DB_USER_ID',
                         passwordVariable: 'MY_DB_PASS',
                         usernameVariable: 'MY_DB_USER')]) {
            sh './gradlew clean test -Duser=$MY_DB_USER -Dpass=$MY_DB_PASS'
        }
    }
}