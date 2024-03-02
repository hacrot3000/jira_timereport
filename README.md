# jira_timereport
Free time report plugin for Atlassian JIRA (self hosted version)

Here are the SDK commands you'll use immediately:

* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-help  -- prints description for all commands in the SDK
* atlas-package -- Generate the compiled jar file

Full documentation is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK



To install plugin:

* Atlassian Universal Plugin Manager: The Universal Plugin Manager (UPM) is a tool for administering apps in Atlassian applications. You can use the UPM to find and install, manage, and configure apps. The UPM interface is the same across products. Check the UPM version whether it is compatible with your Jira version or not.
* File size limit: 
* Because of the file size restriction, plugin jar may not be uploaded. Perform the below steps for changing the accepted attachment size:
  * Navigate to Jira Administration -> System
  * Select Advanced -> Attachments
    * Click Edit Settings, which opens the Edit Attachment Settings dialog.
    * In Attachment Size, specify the maximum attachment size. The default (per file) is 1 GB. The maximum attachment size (per file) is 2 GB.
    * Click Update.
  * If you still face any issue while uploading then you need to install the plugin manually. Please perform the below steps for installing plugin manually:
    * Navigate to <jira-home>-> plugins-> installed-plugins folder.
    * Delete the jar file that begins with plugin_ & contains the respective keyword in the name.
      * For SAML, it has jira-sso in the name.
      * For OAuth, it has jira-oauth in the name.
      * For Two Factor, it has two-factor in the name.
    * Add the plugin’s jar file at the same location that you want to install.
    * Restart the server
