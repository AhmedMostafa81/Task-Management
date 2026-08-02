# Logging Quick Reference Guide

## Files Modified

### Controllers (2 files)
1. **AuthController.java** - Added logging to register/login endpoints
   - Request start/end logging
   - Execution time tracking
   - Error handling with details

2. **TaskController.java** - Added logging to all CRUD operations
   - User context tracking
   - Operation details (filters, IDs, etc.)
   - Performance metrics

### Services (2 files)
1. **AuthService.java** - Added logging to authentication logic
   - Step-by-step process tracking
   - Database operations
   - JWT generation

2. **TaskService.java** - Added logging to task operations
   - CRUD operation tracking
   - Filter and query details
   - Access control verification

### Security (2 files)
1. **JwtAuthenticationFilter.java** - Enhanced JWT filter logging
   - Request path and method
   - Token validation steps
   - Authentication results

2. **JwtService.java** - Added JWT operation logging
   - Token generation and validation
   - Claim extraction
   - Expiration checks

### Configuration (1 file)
1. **application.yaml** - Added comprehensive logging configuration
   - Log levels per package
   - Output format (console + file)
   - File rotation settings

---

## What Gets Logged

### Authentication Flow
```
[Register] → Check username → Encrypt password → Save to DB → Generate JWT
   ↓          ↓                ↓                   ↓             ↓
  INFO       DEBUG            DEBUG               INFO          DEBUG
```

```
[Login] → Authenticate → Fetch user → Generate JWT
   ↓        ↓             ↓             ↓
  INFO     DEBUG         DEBUG         DEBUG
```

### Task Operations
```
[Create] → Validate → Build object → Save to DB → Create DTO
   ↓        ↓           ↓              ↓            ↓
  INFO     DEBUG       DEBUG          INFO         DEBUG

[Read]  → Query → Convert to DTO
   ↓      ↓        ↓
  INFO   DEBUG    DEBUG

[Update] → Verify access → Update fields → Save → Create DTO
    ↓          ↓              ↓             ↓      ↓
   INFO       DEBUG          DEBUG        INFO    DEBUG

[Delete] → Verify access → Delete → Confirm
    ↓          ↓             ↓        ↓
   INFO       DEBUG         INFO     INFO
```

### Security/JWT
```
[Request] → Extract JWT → Verify token → Extract username → Load user → Authenticate
    ↓          ↓             ↓              ↓                 ↓          ↓
   DEBUG      DEBUG         DEBUG          DEBUG             DEBUG      INFO
```

---

## Key Information Logged

### For Each Endpoint Call:
- ✅ Timestamp (yyyy-MM-dd HH:mm:ss.SSS)
- ✅ Thread name (for concurrency debugging)
- ✅ Log level (DEBUG, INFO, WARN, ERROR)
- ✅ Class and method name
- ✅ Username (audit trail)
- ✅ Resource ID (task ID, etc.)
- ✅ Execution time (in milliseconds)
- ✅ Input parameters (filters, data)
- ✅ Operation result (success/failure)
- ✅ Error details with stack traces

---

## Log Output Examples

### Successful Registration
```
2026-08-02 14:28:11 [http-nio-8080-exec-1] INFO com.internship.backend.controller.AuthController - Processing registration request for username: alice
2026-08-02 14:28:11 [http-nio-8080-exec-1] DEBUG com.internship.backend.service.AuthService - Starting registration process for username: alice
2026-08-02 14:28:11 [http-nio-8080-exec-1] DEBUG com.internship.backend.service.AuthService - Username availability check passed for: alice
2026-08-02 14:28:11 [http-nio-8080-exec-1] DEBUG com.internship.backend.service.AuthService - Encrypting password for username: alice
2026-08-02 14:28:11 [http-nio-8080-exec-1] DEBUG com.internship.backend.service.AuthService - Saving user to database: alice
2026-08-02 14:28:12 [http-nio-8080-exec-1] INFO com.internship.backend.service.AuthService - User saved successfully to database: alice
2026-08-02 14:28:12 [http-nio-8080-exec-1] DEBUG com.internship.backend.service.AuthService - Generating JWT token for username: alice
2026-08-02 14:28:12 [http-nio-8080-exec-1] INFO com.internship.backend.service.AuthService - Registration completed successfully for username: alice
2026-08-02 14:28:12 [http-nio-8080-exec-1] INFO com.internship.backend.controller.AuthController - Registration successful for username: alice - Duration: 1245ms
```

### Task Creation with Error
```
2026-08-02 14:28:13 [http-nio-8080-exec-2] INFO com.internship.backend.controller.TaskController - Creating new task for user: bob - Title: My Task
2026-08-02 14:28:13 [http-nio-8080-exec-2] DEBUG com.internship.backend.service.TaskService - Creating new task for user: bob - Title: 'My Task', Priority: HIGH, Status: PENDING
2026-08-02 14:28:13 [http-nio-8080-exec-2] DEBUG com.internship.backend.service.TaskService - Built task object - preparing to save for user: bob
2026-08-02 14:28:13 [http-nio-8080-exec-2] ERROR com.internship.backend.controller.TaskController - Error creating task for user: bob - Duration: 245ms
java.sql.SQLException: Connection timeout
```

### Access Denied
```
2026-08-02 14:28:14 [http-nio-8080-exec-3] INFO com.internship.backend.controller.TaskController - Fetching task ID: 999 for user: charlie
2026-08-02 14:28:14 [http-nio-8080-exec-3] DEBUG com.internship.backend.service.TaskService - Fetching task ID: 999 for user: charlie
2026-08-02 14:28:14 [http-nio-8080-exec-3] DEBUG com.internship.backend.service.TaskService - Validating task access - Task ID: 999, User: charlie
2026-08-02 14:28:14 [http-nio-8080-exec-3] WARN com.internship.backend.service.TaskService - Task ID: 999 not found for user: charlie or user does not have access
```

---

## How to Use Logs for Debugging

### Find Issues by Log Level
```bash
# All errors
grep "ERROR" logs/application.log

# Authentication issues
grep -i "auth" logs/application.log | grep "WARN\|ERROR"

# Slow operations (>1000ms)
grep "Duration:" logs/application.log | awk '{print $(NF-2), $(NF-1), $NF}' | sort -n
```

### Track User Activity
```bash
# All activities by user 'alice'
grep "alice" logs/application.log

# Failed login attempts
grep "alice" logs/application.log | grep "Authentication failed"
```

### Monitor Task Operations
```bash
# All task operations
grep "TaskController\|TaskService" logs/application.log

# Failed task access
grep "access denied" logs/application.log
```

### Performance Analysis
```bash
# Find slow operations
grep "Duration:" logs/application.log | awk '{
  split($NF, a, "ms");
  if (a[1] > 1000) print $0
}'
```

---

## Logging Levels Explained

| Level | Usage | Count |
|-------|-------|-------|
| **DEBUG** | Detailed execution steps, parameter values, conditional branches | ~50+ per request |
| **INFO** | Successful operations, completions, important state changes | ~10-15 per request |
| **WARN** | Validation failures, expected errors, security issues | 0-5 per request |
| **ERROR** | Unexpected exceptions, system failures | 0-1 per request |

---

## Configuration File Location

- **File**: `backend/src/main/resources/application.yaml`
- **Logging Config**: Lines with `logging:` section

### To Change Log Levels:
```yaml
logging:
  level:
    com.internship.backend.controller: DEBUG  # Change this
    com.internship.backend.service: DEBUG     # Or this
```

### To Change Output:
```yaml
logging:
  file:
    name: logs/application.log  # File location
    max-size: 10MB              # Rotation size
    max-history: 10             # Number of archived files
```

---

## Tips for Effective Logging

### ✅ Do's
- Use meaningful variable names in logs
- Include user context (username, ID)
- Log important state changes
- Track execution time for performance
- Use appropriate log levels
- Include resource IDs for traceability

### ❌ Don'ts
- Don't log passwords or tokens
- Don't log sensitive personal data
- Don't log too much (impacts performance)
- Don't use WARN for expected conditions
- Don't ignore stack traces in ERROR logs

---

## Integration with Monitoring Tools

The logs are structured for easy parsing:
- **Splunk**: Parse by timestamp and log level
- **ELK Stack**: Kibana visualization of logs by level
- **Datadog**: APM integration for performance metrics
- **CloudWatch**: AWS log group streaming
- **New Relic**: Application performance monitoring

All logs include:
- Consistent timestamp format
- Structured messages
- Resource identifiers
- Execution metrics
- Error categorization

---

## Need Help?

For logging issues or questions:
1. Check `logs/application.log` for errors
2. Review this guide for log format examples
3. Adjust log levels in `application.yaml` if needed
4. Search logs by username or task ID for specific issues
