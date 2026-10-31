# Modernization Plan: Selected AppCAT Findings

**Project**: biblioteca-facil
**Assessment report**: report-20261028190826

## Technical Framework

- **Language**: Java 25
- **Framework**: None detected
- **Build Tool**: Maven
- **Database**: MySQL
- **Key Dependencies**: MySQL Connector/J 8.4.0, Java Swing

## Overview

This plan addresses only the five categories selected from the assessment report. It covers externalized configuration, migration from the current MySQL/local JDBC setup, modernization of the Swing interface, and container readiness. It does not include provisioning, deployment, CVE remediation, or integration testing.

## Selected Assessment Scope

| Category | Issue | Solution |
|---|---|---|
| Configuration Management (Environment Variables) | Environment variables/system properties | Configure System Environment Variables |
| Database Migration (MySQL) | MySQL database found | Migrate to Azure Database for MySQL [kbId: mi-mysql] |
| Desktop Gui Migration (Javax Swing) | Java Swing usage | Redesign Java GUI application to migrate it to Azure |
| Local Resource Access (Localhost) | Local JDBC Calls | Migrate the Local Resource to Azure |
| Containerization | No Dockerfile found | Containerize Java Application for Container Readiness [kbId: containerization-copilot-agent] |

## Phases

1. Externalize application configuration.
2. Migrate MySQL and remove local JDBC resource assumptions.
3. Redesign the Swing interface for the Azure-targeted application.
4. Containerize the updated application.

Tasks are sequenced to avoid overlapping source changes. Containerization follows the application changes. No Azure resource provisioning or deployment is included.

## Migration Impact Summary

| Application | Original Service | New Azure Service | Authentication | Comments |
|---|---|---|---|---|
| biblioteca-facil | Local MySQL | Azure Database for MySQL | Not specified | Selected database migration |
| biblioteca-facil | Java Swing desktop GUI | Azure-targeted GUI | Not applicable | Redesign selected by user |
| biblioteca-facil | Local JDBC resource | Azure resource | Not specified | Remove localhost resource dependency |
| biblioteca-facil | No container image | Container-ready application | Not applicable | Containerization only; no deployment |
