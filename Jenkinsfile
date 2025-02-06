pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'echo "Build 작업 수행"'
            }
        }

        stage('Deploy') {
            steps {
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'taiwan-walking-map-ec2', // 설정한 SSH 서버 이름
                            transfers: [
                                sshTransfer(
                                    sourceFiles: '**/*.jar',  // 예제: jar 파일 배포
                                    remoteDirectory: '/var/www/my_project/',
                                    removePrefix: 'target',
                                    execCommand: '''
                                        cd /var/www/my_project/
                                        chmod +x my_project.jar
                                        nohup java -jar my_project.jar > output.log 2>&1 &
                                    '''
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }
}
