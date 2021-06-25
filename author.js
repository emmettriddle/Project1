let url = "localhost8080://Project1/controller";



function currentStories(){let flag = "/currentStories";
let xhttp = new XMLHttpRequest();
xhttp.open("GET", url + flag, true);
xhttp.send();
xhttp.onreadystatechange = () => {
    if (xhttp.readyState==4 && xhttp.status==200){
        console.log("All Stories")
        
    }
}}

function requests(){}

function newPitch(){}