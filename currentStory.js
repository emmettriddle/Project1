let url = "http://localhost:8080/Project_1/controller";

function fillst(){
    let flag = "/get_story_from_session";
    let xhttp = new XMLHttpRequest();
    xhttp.open("GET", url + flag, true);
    xhttp.send();
    xhttp.onreadystatechange = () => {
        if (xhttp.readyState==4 && xhttp.status==200){
            console.log("All Stories")
            console.log(xhttp.responseText);
            let stories = JSON.parse(xhttp.responseText);
            let table = document.getElementById("story");
            for (let story of stories){
                let tr = document.createElement("tr");
                //tr.setAttribute("class", "table table-dark table-striped table-hover");

                let td = document.createElement("td");
                //td.setAttribute("class", "table table-dark table-striped table-hover");
                td.innerHTML = story.title;
                td.onclick = () => {handleRowClick(story)}
                tr.appendChild(td)

                 td = document.createElement("td");
                td.innerHTML = story.genre.name;
                td.onclick = () => {handleRowClick(story)}
                tr.appendChild(td)

                 td = document.createElement("td");
                td.innerHTML = story.type.name;
                td.onclick = () => {handleRowClick(story)}
                tr.appendChild(td)

                 td = document.createElement("td");
                td.innerHTML = story.approvalStatus;
                td.onclick = () => {handleRowClick(story)}
                tr.appendChild(td)

                table.appendChild(tr);
            }
        }
    }
}

function handleRowClick(story){
    let flag = "/saveStoryToSession";
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", url + flag, true);
    xhttp.send(JSON.stringify(story));
    xhttp.onreadystatechange = () => {
        if (xhttp.readyState==4 && xhttp.status==200){

            window.location.href = "staticPropForm.html";
        }
    }

}



