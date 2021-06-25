# Project 1 Requirements
> # Tuition Reimbursement Management System
- # Employees can submit reimbursement requests
    - Different types of courses have different coverage
        - ***Maximum amount of reimbursement is $1000/year and resets every year***
        - Certification: 100%
        - Technical Training: 90%
        - University Courses: 80%
        - Certification Preparation Classes: 75%
        - Seminars: 60%
        - Other: 30%
    - After a Benefits Coordinator approves the request, it is marked as pending until a passing grade or presentation about the event is provided.
    - Monetary amount available for reimbursement calculated as follows:
        > Avialable Reimbursement = Total Reimbursement ($1000) - Pending Reimbursements - Awarded Reimbursements
    - If a projected reimbursement exceeds the available reimbursement amount, it is adjusted to fit the amount avialable.

    ### **Reimbursement Form**
    - Must be completed **one week** prior to the start of the event
    - Must include the following information:
        - basic information about the employee
        - date, time, location, description, cost, grading format, and type of event (course materials (ie. books) should not be included in the cost)
        - work-related justification
    - Optional inclusions:
        - event-related attachments
            - .pdf, .png, .jpg, .txt, or .doc file types
        - approvals already provided
            - .msg (Outlook Email) file type
            - type of approval
        - work time that will be missed
    - Projected reimbursement should be provided as a read-only field

    ### **Business Rules**
    - Grading formats pulled from a reference table.
        - Certain grading formats require the employee to perform a presentation to management prior to awarded reimbursement.
        - For formats that do not require a presentation, a passing grade must be provided.
            - Employee must provide the passing grade cutoff for the course, or use a default passing grade if unknown.
            - If employee provides an approval email, this step is skipped. (Cannot skip Benefits Coordinator approval)
        - If course starts less than 2 weeks from reimbursement submission, it is marked as urgent.
- # Approval Process
    - ## Direct Supervisor Approval
        - A Direct Supervisor must approve a request.
        - The Direct  Supervisor can request more information from the employee before approving the reimbursement, if desired.
        - If the reimbursement is denied, a reason must be given to the employee.
        - If the Direct Supervisor is also a Department Head, this step is skipped.
        - If the request is not processed in a timely manner, the request is automatically approved.
    - ## Department Head Approval
        - The Department Head must approve a request.
        - The Department Head can request additional information from the employee or direct superviso before approving the reimbursement, if desired.
        - If the request is not processed in a timely manner, the request is automatically approved.
    - ## Benefits Coordinator Approval
        - The Benefits Coordinator must approve a request.
        - This step is not skippable for any reason.
        - The Benefits Coordinator can request additional information from the employee, direct supervisor, or department head before approval, if desired.
        - The Benefits Coordinator has the ability to alter the reimbursement amount.
            - If the reimbursement amount is changed, the employee should be notified, and should have the option to approve or cancel the request.
        - If the Benefits Coordinator does not process the request in a timely manner, an escalation email should be sent to the Benefits Coordinator's direct supervisor.
        - The Benefits Coordinator has the ability to award an amount larger than the amount available for the employee.
            - If an amount exceeding the available amount is awarded, a reason must be given, and the reimbursement must be marked as exceeding available funds for reporting purposes.
- # Grade/Presentation Upload
    - Upon event completion, the employee should attach either the grade or presentation as appropriate.
    - After upload of a grade, the Benefits Coordinator must confirm that the grade is passing.
    - After upload of a presentation, the Direct Manager must confirm that the presentation was satisfactory and presented to the appropriate parties.
    - Upon confirmation, the reimbursement amound is awarded to the employee.
- # Miscellaneous
    - Only the employee and the appropriate approval parties should be able to view the reimbursement request.