let url = "http://localhost:8080/Project_1/controller"

function fillDropdowns() {
    console.log("fillStoryTypeDropdown");
    let flag = "/get_story_types";
    let sts = document.getElementById("story_type_select");
    let gs = document.getElementById("genre_select");
    let point_counter = document.getElementById("point_counter");

    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", url + flag, true);
    xhttp.send();

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                let rt = xhttp.responseText;
                let jsons = JSON.parse(rt);
                let stj = JSON.parse(jsons[0]);
                let gj = JSON.parse(jsons[1]);
                let author = JSON.parse(jsons[2]);

                for (let i in stj) {
                    let story_type = stj[i];
                    let option = document.createElement("option");
                    if (i == 0) {
                        option.setAttribute("selected", "selected");
                    }
                    option.setAttribute("value", story_type.name);
                    option.setAttribute("cost", story_type.points);
                    option.innerHTML = story_type.name;
                    sts.appendChild(option);
                }

                point_counter.innerHTML = "Point Cost: " + stj[0].points;
                sts.onchange = () => {
                    point_counter.innerHTML = "Point Cost: " + sts.children[sts.selectedIndex].getAttribute("cost");
                }

                for (let i in gj) {
                    let genre = gj[i];
                    let option = document.createElement("option");
                    if (i == 0) {
                        option.setAttribute("selected", "selected");
                    }
                    option.setAttribute("value", genre.name);
                    option.innerHTML = genre.name;
                    gs.appendChild(option);
                }

                let author_name = document.getElementById("author_name");
                let author_bio = document.getElementById("author_bio");
                let author_points = document.getElementById("points_available");

                author_name.innerHTML = author.firstName + " " + author.lastName;
                author_bio.innerHTML = author.bio;
                author_points.innerHTML = "Available Points: " + author.points;
            }
        }
    }
}

function submitForm() {
    console.log("submitForm");
    let flag = "/submit_story_form";
    let story_form = {
        title: document.getElementById("story_title").value,
        genre: document.getElementById("genre_select").value,
        type: document.getElementById("story_type_select").value,
        description: document.getElementById("description").value,
        tagline: document.getElementById("tagline").value,
        date: document.getElementById("date").value
    }
    
    let json = JSON.stringify(story_form);
    console.log(json);
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(json);

    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4) {
            if (xhttp.status == 200) {
                console.log("Received response from form submission!");
                // window.location.href = xhttp.responseText;
            }
        }
    }
}