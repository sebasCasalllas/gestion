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

        stage('Compilar y Probar') {
            steps {
                // Ejecutamos los tests de tu proyecto
                sh './gradlew clean test'
            }
        }
    }
}