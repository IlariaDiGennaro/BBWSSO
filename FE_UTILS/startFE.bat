cd C:\Users\ilari\git\BBWSSO\App1\target

start /B java -jar app1-1.0.0.war

start /B java -jar app1-1.0.0.war --server.port=8091 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App2\\App2E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App2\\App2C.cer --this.app.identifier=App2 --other.app.identifiers=App1-App3-App4-App5-App6-App7-App8-App9-App10 --spring.data.mongodb.database=MDBApp2 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App2\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8092 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App3\\App3E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App3\\App3C.cer --this.app.identifier=App3 --other.app.identifiers=App1-App2-App4-App5-App6-App7-App8-App9-App10 --spring.data.mongodb.database=MDBApp3 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App3\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8093 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App4\\App4E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App4\\App4C.cer --this.app.identifier=App4 --other.app.identifiers=App1-App2-App3-App5-App6-App7-App8-App9-App10 --spring.data.mongodb.database=MDBApp4 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App4\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8094 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App5\\App5E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App5\\App5C.cer --this.app.identifier=App5 --other.app.identifiers=App1-App2-App3-App4-App6-App7-App8-App9-App10 --spring.data.mongodb.database=MDBApp5 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App5\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8095 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App6\\App6E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App6\\App6C.cer --this.app.identifier=App6 --other.app.identifiers=App1-App2-App3-App4-App5-App7-App8-App9-App10 --spring.data.mongodb.database=MDBApp6 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App6\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8096 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App7\\App7E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App7\\App7C.cer --this.app.identifier=App7 --other.app.identifiers=App1-App2-App3-App4-App5-App6-App8-App9-App10 --spring.data.mongodb.database=MDBApp7 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App7\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8097 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App8\\App8E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App8\\App8C.cer --this.app.identifier=App8 --other.app.identifiers=App1-App2-App3-App4-App5-App6-App7-App9-App10 --spring.data.mongodb.database=MDBApp8 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App8\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8098 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App9\\App9E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App9\\App9C.cer --this.app.identifier=App9 --other.app.identifiers=App1-App2-App3-App4-App5-App6-App7-App8-App10 --spring.data.mongodb.database=MDBApp9 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App9\log4j2.yaml

start /B java -jar app1-1.0.0.war --server.port=8099 --encryption.key.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App10\\App10E.key --x509.certificate.full.path=C:\\Users\\ilari\\Documents\\TESI_CER\\App10\\App10C.cer --this.app.identifier=App10 --other.app.identifiers=App1-App2-App3-App4-App5-App6-App7-App8-App9 --spring.data.mongodb.database=MDBApp10 --logging.config=C:\Users\ilari\Documents\TESI_LOG\App10\log4j2.yaml