/var/lib/tomcat7/webapps/ROOT:
  file:
    - absent

/usr/share/tomcat7/openam:
  file:
    - absent

/usr/share/tomcat7/.openamcfg:
  file:
    - absent

openam-flush:
  cmd.run:
    - name: 'service tomcat7 stop;sleep 30;service tomcat7 start;sleep 180'
    - user: root