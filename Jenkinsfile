pipeline {
    agent any

    tools {
        // Aquí usamos los nombres que configuraste en "Tools"
        jdk 'JDK21'
        gradle 'Gradle_Latest'
    }

    environment {
        DB_HOST = 'db-estudio'
        DB_PORT = '5432'
    }

    stages {
        stage('Explorar') {
            steps {
                // Un paso para verificar que todo esté en orden
                sh 'java -version'
                sh './gradlew --version'
            }
        }
        stage('Ejecutar Tests') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'db_user',
                                 passwordVariable: 'MY_DB_PASS',
                                 usernameVariable: 'MY_DB_USER')]) {
                    sh './gradlew clean test -Duser=$MY_DB_USER -Dpass=$MY_DB_PASS'
                }
            }
        }
        stage('Análisis en SonarCloud') {
            steps {
                withCredentials([
                    string(credentialsId: 'SONAR_TOKEN', variable: 'S_TOKEN'),
                    string(credentialsId: 'SONAR_PROJECT_KEY', variable: 'S_PROJECT'),
                    string(credentialsId: 'SONAR_ORG', variable: 'S_ORG')
                ]) {
                    sh """
                        ./gradlew sonar \
                        -Dsonar.token=${S_TOKEN} \
                        -Dsonar.projectKey=${S_PROJECT} \
                        -Dsonar.organization=${S_ORG} \
                        -Dsonar.coverage.jacoco.xmlReportPaths=build/reports/jacoco/test/jacocoTestReport.xml
                    """
                }
            }
        }
    }
}