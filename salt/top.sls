base:
  '*':
    - requirements.fslimits
    - requirements.essential
    - requirements.jdk7

  'sso.acorn.ads.example.com':
    - openam12.tomcat7
    - openam12.cleanup
    - openam12.binaries

  'useracctmgmt.acorn.ads.example.com':
    - appserver.tomcat7
    - apps.useracctmgmt

  'externalapp.acorn.ads.example.com':
    - appserver.tomcat7
    - apps.externalapp
