base:
  '*':
    - requirements.fslimits
    - requirements.essential
    - requirements.jdk7

  'sso.acorn.ads.example.com':
    - openam12.tomcat7
    - openam12.binaries

  'useracct.acorn.ads.example.com':
    - appserver.tomcat7
    - apps.useracct

  'externalapp.acorn.ads.example.com':
    - appserver.tomcat7
    - apps.externalapp
