tomcat7:
  file:
    - managed
    - name: /etc/default/tomcat7
    - source: salt://appserver/tomcat7.default

  pkg:
    - installed
    - require:
      - file: tomcat7

  service:
    - running
    - enable: True
    - require:
      - pkg: tomcat7
    - watch:
      - file: tomcat7
      - file: /etc/tomcat7/server.xml
      - file: /var/lib/tomcat7/webapps/ROOT.war

/etc/tomcat7/server.xml:
  file:
    - managed
    - source: salt://appserver/server.xml
    - user: root
    - group: tomcat7
    - mode: 644

/var/lib/tomcat7/webapps/ROOT:
  file:
    - absent