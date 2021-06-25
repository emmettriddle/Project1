let url = "http://localhost:8080/Project_1/controller";

function populateLabels() {
    let flag = "/get_editor_main_labels"

    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                let logged_in_label = document.getElementById("logout_button_label");
                let proposals_label = document.getElementById("proposals_button_label");
                let info_label = document.getElementById("info_button_label");
                let drafts_label = document.getElementById("drafts_button_label");

                let values = JSON.parse(xhttp.responseText);

                logged_in_label.innerHTML = "Logged in as: " + values[0];
                proposals_label.innerHTML = "Pending: " + values[1];
                info_label.innerHTML = "Pending: " + values[2];
                drafts_label.innerHTML = "Pending: " + values[3];
            }
        }
    }
}

function viewProposals() {
    window.location.href = "editor_story_list.html";
}

function viewInfoRequests() {
    window.location.href = "info_request_list.html";
}

function viewDrafts() {
    window.location.href = "draft_list.html";
}

function logout() {
    let flag = "/logout";
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                console.log("Logged out!");
                window.location.href = xhttp.responseText;
            }
        }
    }
}