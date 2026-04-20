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

        stage('Compilar y Probar') {
            steps {
                // Ejecutamos los tests de tu proyecto
                sh './gradlew clean test'
            }
        }
    }
}