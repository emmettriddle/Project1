let url = "http://localhost:8080/Project_1/controller";

function fillTable() {
    let flag = "/get_draft_requests";

    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                let stories = JSON.parse(xhttp.responseText);

                let table = document.getElementById("table");
                let rows = table.querySelectorAll("[name=\"dynamic\"]");
                console.log(rows);
                for (let elem of rows) {
                    elem.remove();
                }
                for (let story of stories) {
                    let tr = document.createElement("tr");
                    tr.setAttribute("name", "dynamic");
                    
                    // Author
                    let td = document.createElement("td");
                    td.setAttribute("class", "green purple-background");
                    td.innerHTML = story.author.firstName + " " + story.author.lastName;
                    td.onclick = () => {
                        handleRowClick(story);
                    }
                    tr.appendChild(td);

                    // Title
                    td = document.createElement("td");
                    td.setAttribute("class", "green purple-background");
                    td.innerHTML = story.title;
                    td.onclick = () => {
                        handleRowClick(story);
                    }
                    tr.appendChild(td);

                    // Story Name
                    td = document.createElement("td");
                    td.setAttribute("class", "green purple-background");
                    td.innerHTML = story.type.name;
                    td.onclick = () => {
                        handleRowClick(story);
                    }
                    tr.appendChild(td);

                    // Approval Status
                    td = document.createElement("td");
                    td.setAttribute("class", "green purple-background");
                    td.innerHTML = story.approvalStatus;
                    td.onclick = () => {
                        handleRowClick(story);
                    }
                    tr.appendChild(td);
                    
                    table.appendChild(tr);
                }
            }
        }
    }
}

function handleRowClick(story) {
    let modal_draft = document.getElementById("modal_draft");
    modal_draft.style.display = "block";

    let title = document.getElementById("df_title");
    let author_name = document.getElementById("df_author_name");
    let genre = document.getElementById("df_genre");
    let type = document.getElementById("df_type");
    let draft = document.getElementById("df_draft");

    title.innerHTML = story.title;
    author_name.innerHTML = story.author.firstName + " " + story.author.lastName;
    genre.innerHTML = story.genre.name;
    type.innerHTML = story.type.name;
    draft.innerHTML = story.draft;

    console.log(story.approvalStatus);
    
    let approveBtn = document.getElementById("approve_button");
    // let denyBtn = document.getElementById("deny_button");
    let changeBtn = document.getElementById("change_button");

    approveBtn.onclick = () => {
        approve(story);
    }

    // denyBtn.onclick = () => {
    //     deny(story);
    // }

    changeBtn.onclick = () => {
        story.draft = draft.value;
        story.modified = true;
        change(story);
    }
}

function listBack() {
    window.location.href = "editor_main.html";
    fillTable();
}

function closeModal() {
    let modal_draft = document.getElementById("modal_draft");
    modal_draft.style.display = "none";
    fillTable();
}

function approve(story) {
    let flag = "/approve_draft";

    let json = JSON.stringify(story);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    closeModal();

    let approveBtn = document.getElementById("approve_button");
    // let denyBtn = document.getElementById("deny_button");
    let changeBtn = document.getElementById("change_button");
    
    approveBtn.style.display = "none";
    // denyBtn.style.display = "none";
    changeBtn.style.display = "none";

    xhttp.onreadystatechange = () => {}
}

function deny(story) {
    let flag = "/deny_draft";

    let json = JSON.stringify(story);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {}
}

function change(story) {
    let flag = "/request_draft_change";

    let json = JSON.stringify(story);

    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {}
}