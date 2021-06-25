let url = "http://localhost:8080/Project_1/controller"
let time_limit = 1.21e+9;   // 2 weeks in milliseconds
let logged_in;

/*
        Fill in the list of proposals on editor_story_list.html
*/
function fillProposals() {
    // getLoggedIn();
    console.log("filling proposal table");
    let flag = "/get_proposals";

    let xhttp = new XMLHttpRequest();

    xhttp.open("GET", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                console.log("fillProposals() ready/OK");
                // let rt = xhttp.responseText;
                // let json = JSON.parse(rt);

                let strs = xhttp.responseText.split("|");
                logged_in = strs[0];
                console.log(strs[1]);
                let stories = JSON.parse(strs[1]);

                // console.log(json);

                let lock = false;
                let senior = logged_in == "senior";
                let general = logged_in == "general";
                let assistant = logged_in == "assistant";
                let author = logged_in == "author"

                let table = document.getElementById("proposals");
                for (let story of stories) {
                // for (let story of json) {
                    let overdue = checkOverdue(story);
                    if (overdue) lock = true;
                    // let lockOthers = shouldLockOthers(story);

                    let tr = document.createElement("tr");
                    
                    // Author
                    let td = document.createElement("td");
                    
                    if (overdue && !author) {
                        td.setAttribute("class", "green red-background");
                    } else {
                        td.setAttribute("class", "green purple-background");
                    }
                    td.innerHTML = story.author.firstName + " " + story.author.lastName;

                    console.log("Author: " + author);
                    console.log("Assistant: " + assistant);
                    console.log("General: " + general);
                    console.log("Senior: " + senior);
                    console.log("Overdue: " + overdue);
                    console.log("Lock: " + lock);

                    if ((author || senior) || ((overdue == lock) && (assistant || general))) {
                        td.onclick = () => {
                            handleRowClick(story);
                        }
                    }

                    tr.appendChild(td);

                    // Title
                    td = document.createElement("td");
                    if (overdue && !author) {
                        td.setAttribute("class", "green red-background");
                    } else {
                        td.setAttribute("class", "green purple-background");
                    }
                    td.innerHTML = story.title;

                    if ((author || senior) || ((overdue == lock) && (assistant || general))) {
                        td.onclick = () => {
                            handleRowClick(story);
                        }
                    }

                    tr.appendChild(td);

                    // Story Name
                    td = document.createElement("td");
                    if (overdue && !author) {
                        td.setAttribute("class", "green red-background");
                    } else {
                        td.setAttribute("class", "green purple-background");
                    }
                    td.innerHTML = story.type.name;

                    if ((author || senior) || ((overdue == lock) && (assistant || general))) {
                        td.onclick = () => {
                            handleRowClick(story);
                        }
                    }

                    tr.appendChild(td);

                    // Approval Status
                    td = document.createElement("td");
                    if (overdue && !author) {
                        td.setAttribute("class", "green red-background");
                    } else {
                        td.setAttribute("class", "green purple-background");
                    }
                    td.innerHTML = story.approvalStatus;

                    if ((author || senior) || ((overdue == lock) && (assistant || general))) {
                        td.onclick = () => {
                            handleRowClick(story);
                        }
                    }

                    tr.appendChild(td);
                    
                    table.appendChild(tr);
                }
            }
        }
    }
}

/*
        Handle row clicks on editor_story_list.html
*/
function handleRowClick(story) {
    let flag = "/save_story_to_session";
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(JSON.stringify(story));

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                if (xhttp.responseText == "saved") {
                    console.log("saved story, redirecting!");
                    window.location.href = "story_form_static.html";
                }
            }
        }
    }
}

/*
        Utility functions for editor_story_list.html
 */
function getDateDifference(submitted) {
    const current = new Date();
    submitted = new Date(submitted);
    return current - submitted;
}

function checkOverdue(story) {
    console.log(story.title + " has approval status " + story.approvalStatus);
    return getDateDifference(story.submissionDate) > time_limit;
}

function listBack() {
    if (logged_in == "author") {
        window.location.href = "author_main.html";
    } else {
        window.location.href = "editor_main.html";
    }
}



/*
        Fill fields on story_form_static.html
*/
function populateStaticForm() {
    let flag = "/get_story_from_session";
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        console.log("Clicked " + xhttp.responseText);
        let strs = xhttp.responseText.split("|");
        logged_in = strs[0];
        let story = JSON.parse(strs[1]);

        let isSenior = isSeniorStatus(story);

        if (isSenior) {
            let update_button = document.getElementById("update_button");
            update_button.style.display = "inline";
            update_button.onclick = () => {
                updateDetails(story);
            }
        }

        if (story.response != null) {
            let response_button = document.getElementById("response_button");
            response_button.style.display = "inline";
            response_button.onclick = () => {
                let modal_response = document.getElementById("modal_response");
                modal_response.style.display = "block";

                let request_label = document.getElementById("request_label");
                request_label.innerHTML = "You asked " + story.receiverName + " for the following information:";

                let request = document.getElementById("request");
                request.innerHTML = story.request;

                let response = document.getElementById("response");
                response.innerHTML = story.response;
            }
        }

        let sf_sub_date = document.getElementById("sf_submission_date");
        let sf_title = document.getElementById("sf_title");
        let sf_author_name = document.getElementById("sf_author_name");
        let sf_author_bio = document.getElementById("sf_author_bio");
        let sf_author_points = document.getElementById("sf_author_points");
        let sf_genre = document.getElementById("sf_genre");
        let sf_type = document.getElementById("sf_type");
        let sf_description = document.getElementById("sf_description");
        let sf_tagline = document.getElementById("sf_tagline");
        let sf_completion_date = document.getElementById("sf_completion_date");
        let sf_status = document.getElementById("sf_status");

        console.log(story.draft);
        if (story.approvalStatus == "approved_senior") {
            let sf_draft_label = document.getElementById("sf_draft_label");
            let sf_draft = document.getElementById("sf_draft");

            sf_draft_label.style.display = "block";
            sf_draft.style.display = "block";

            sf_draft.innerHTML = story.draft;
        }

        if (isSenior) {
            var sf_title_editable = document.createElement("input");
            sf_title_editable.setAttribute("type", "text");
            sf_title_editable.setAttribute("name", "story_title");
            sf_title_editable.setAttribute("id", "sf_title");
            sf_title_editable.style.display = "block";
            sf_title.parentNode.replaceChild(sf_title_editable, sf_title);

            var sf_tagline_editable = document.createElement("input");
            sf_tagline_editable.setAttribute("type", "text");
            sf_tagline_editable.setAttribute("name", "tagline");
            sf_tagline_editable.setAttribute("id", "sf_tagline");
            sf_tagline_editable.style.display = "block";
            sf_tagline.parentNode.replaceChild(sf_tagline_editable, sf_tagline);

            var sf_completion_date_editable = document.createElement("input");
            sf_completion_date_editable.setAttribute("type", "text");
            sf_completion_date_editable.setAttribute("name", "completion_date");
            sf_completion_date_editable.setAttribute("id", "sf_completion_date");
            sf_completion_date_editable.style.display = "block";
            sf_completion_date.parentNode.replaceChild(sf_completion_date_editable, sf_completion_date);
        }

        let overdue = checkOverdue(story);

        sf_sub_date.innerHTML = story.submissionDate;

        if (overdue) {
            sf_sub_date.innerHTML += "    ** High Priority **";
        }



        if (isSenior) {
            sf_title_editable.value = story.title;
            sf_tagline_editable.value = story.tagLine;
            sf_completion_date_editable.value = story.completionDate;
        } else {
            sf_title.innerHTML = story.title;
            sf_tagline.innerHTML = story.tagLine;
            sf_completion_date.innerHTML = story.completionDate;
        }
        sf_author_name.innerHTML = story.author.firstName + " " + story.author.lastName;
        sf_author_bio.innerHTML = story.author.bio;
        sf_author_points.innerHTML = story.author.points + " (" + (story.author.points - story.type.points) + " points if approved)";
        sf_genre.innerHTML = story.genre.name;
        sf_type.innerHTML = story.type.name + " (" + story.type.points + " points)";
        sf_description.innerHTML = story.description;
        sf_status.innerHTML = story.approvalStatus;



        if (overdue) {
            sf_sub_date.setAttribute("class", "red light-grey-background");
        } else {
            sf_sub_date.setAttribute("class", "purple light-grey-background");
        }

        if (isSenior) {
            sf_title_editable.setAttribute("class", "purple light-grey-background");
            sf_tagline_editable.setAttribute("class", "purple light-grey-background");
            sf_completion_date_editable.setAttribute("class", "purple light-grey-background");
        } else {
            sf_title.setAttribute("class", "purple light-grey-background");
            sf_tagline.setAttribute("class", "purple light-grey-background");
            sf_completion_date.setAttribute("class", "purple light-grey-background");
        }
        
        sf_author_name.setAttribute("class", "purple light-grey-background");
        sf_author_bio.setAttribute("class", "purple light-grey-background");
        sf_author_points.setAttribute("class", "purple light-grey-background");
        sf_genre.setAttribute("class", "purple light-grey-background");
        sf_type.setAttribute("class", "purple light-grey-background");
        sf_description.setAttribute("class", "purple light-grey-background");
        sf_status.setAttribute("class", "purple light-grey-background");



        let approveBtn = document.getElementById("approve_button");
        let denyBtn = document.getElementById("deny_button");
        let infoBtn = document.getElementById("info_button");
        let draftBtn = document.getElementById("draft_button");

        if (logged_in == "author") {
            infoBtn.style.display = "none";

            if (story.approvalStatus == "approved_senior") {
                draftBtn.style.display = "inline";

                let modal_draft = document.getElementById("modal_draft");
                draftBtn.onclick = () => {
                    modal_draft.style.display = "block";
                }

                let draft_title = document.getElementById("draft_title");
                draft_title.innerHTML = story.title;

                let submit_draft_button = document.getElementById("submit_draft_button");
                submit_draft_button.onclick = () => {
                    submitDraft(story);
                }

                if (!story.modified) {
                    approveBtn.style.display = "none";
                    denyBtn.style.display = "none";
                }
            }
        }

        approveBtn.onclick = () => {
            approve(story);
        }

        /*
                Approve proposal
        */
        function approve(story) {
            let flag = "/approve_story";

            if (logged_in == "author" && story.modified) {
                story.modified = false;
            } else {
                story.request = null;
                story.response = null;
                story.requestorName = null;
                story.receiverName = null;
            }
        
            let json = JSON.stringify(story);
        
            let xhttp = new XMLHttpRequest();
            xhttp.open("POST", url + flag, true);
            xhttp.send(json);
        
            xhttp.onreadystatechange = () => {
                if (xhttp.readyState == 4) {
                    if (xhttp.status == 200) {
                        sf_status.innerHTML = "Approved";
                    }
                }
            }
        }

        

        /* Handle Modal Reason */
        let modal_reason = document.getElementById("modal_reason");
        denyBtn.onclick = () => {
            // deny(story);
            modal_reason.style.display = "block";
        }

        let sendReasonBtn = document.getElementById("send_reason_button");
        sendReasonBtn.onclick = () => {
            let reason = document.getElementById("reason").value;
            if (reason.length > 0) {
                modal_reason.style.display = "none";
                deny(story, reason);
            }
        }
        
        /* Handle Modal Info */
        let modal_info = document.getElementById("modal_info");
        infoBtn.onclick = () => {
            modal_info.style.display = "block";
        }

        let sendInfoBtn = document.getElementById("send_info_button");
        sendInfoBtn.onclick = () => {
            let prompt = document.getElementById("prompt").value;
            let person = document.getElementById("person_select").value;
            if (prompt.length > 0) {
                modal_info.style.display = "none";
                story.request = prompt;
                requestInfo(story, person);
            }
        }



        /* Fill Persons Dropdown */
        let ps = document.getElementById("person_select");
        function makeOption(person, selected) {
            let option = document.createElement("option");
            if (selected) {
                option.setAttribute("selected", "selected");
            }
            option.setAttribute("value", person.firstName + " " + person.lastName);
            option.innerHTML = person.firstName + " " + person.lastName;
            ps.appendChild(option);
        }

        makeOption(story.author, true);

        if (story.approvalStatus == "approved_assistant") {
            makeOption(story.assistant, false);
        }

        if (story.approvalStatus == "approved_editor") {
            makeOption(story.editor, false);
        }
    }
}

/*
        Deny proposal
*/
function deny(story, reason) {
    //
    //  TODO: create pop-up or other method of providing a reason for denial.
    //
    let flag = "/deny_story";

    story.approvalStatus = "denied";
    story.reason = reason;

    let json = JSON.stringify(story);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                console.log("Denied story " + json);
                story = JSON.parse(json);

                let reason_box_label = document.getElementById("label_for_sf_reason");
                let reason_box = document.getElementById("sf_reason");

                reason_box_label.style.display = "block";
                reason_box.style.display = "block";

                reason_box.innerHTML = story.reason;
            }
        }
    }
}

/*
        Request Info about proposal
*/
function requestInfo(story, person) {
    let flag = "/request_info";

    let packet = [JSON.stringify(story), JSON.stringify(person)];

    let json = JSON.stringify(packet);
    console.log("sending request for information! - " + json);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                console.log("Requesting more information for " + story);
            }
        }
    }
}

/*
        Utility functions for story_form_static.html
*/
function sfBack() {
    window.location.href = "editor_story_list.html";
}

function closeInfoModal() {
    let modal_info = document.getElementById("modal_info");
    modal_info.style.display = "none";
}

function closeReasonModal() {
    let modal_reason = document.getElementById("modal_reason");
    modal_reason.style.display = "none";
}

function closeResponseModal() {
    let modal_response = document.getElementById("modal_response");
    modal_response.style.display = "none";
}

function closeDraftModal() {
    let modal_draft = document.getElementById("modal_draft");
    modal_draft.style.display = "none";
}

function isSeniorStatus(story) {
    return story.approvalStatus == "approved_editor";
}

function updateDetails(story) {
    let flag = "/update_details";

    var sf_title_editable = document.getElementById("sf_title");
    var sf_tagline_editable = document.getElementById("sf_tagline");
    var sf_completion_date_editable = document.getElementById("sf_completion_date");

    story.title = sf_title_editable.value;
    story.tagLine = sf_tagline_editable.value;
    story.completionDate = sf_completion_date_editable.value;
    story.modified = true;

    let json = JSON.stringify(story);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                
            }
        }
    }
}

function submitDraft(story) {
    let flag = "/submit_draft";
    let draft_box = document.getElementById("draft_box");
    let xhttp = new XMLHttpRequest();

    story.draft = draft_box.value;
    let json = JSON.stringify(story);

    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {

            }
        }
    }
}