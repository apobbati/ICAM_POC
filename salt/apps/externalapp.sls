externalapp-war:
  file.symlink:
    - name: /var/lib/tomcat7/webapps/ROOT.war
    - target: /srv/repo/ExternalApp/target/ExternalApp.war
    - user: tomcat7
    - group: tomcat7
    - force: True
    - require:
      - pkg: tomcat7