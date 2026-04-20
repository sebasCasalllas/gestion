pipeline {
    agent any

    tools {
        // Aquí usamos los nombres que configuraste en "Tools"
        jdk 'JDK21'
        gradle 'Gradle_Latest'
    }

    environment {
            MY_DB_USER = credentials('MY_DB_USER')
            MY_DB_PASS = credentials('MY_DB_PASS')

            // También puedes agregar variables que no son secretas directamente:
            SPRING_DATASOURCE_URL = 'jdbc:postgresql://db-estudio:5432/gestion_ganado'
    }

    stages {
        stage('Explorar') {
            steps {
                // Un paso para verificar que todo esté en orden
                sh 'java -version'
                sh './gradlew --version'
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
}