
document.addEventListener("DOMContentLoaded", function() {
 let audioPlayer = document.getElementById("audioPlayer");
 audioPlayer.controls = false;
 audioPlayer.volume = 0.1;

 let volumeControlContainer = document.createElement("div");
 volumeControlContainer.id = "volumeControlContainer";

 let volumeControlLabel = document.createElement("label");
 volumeControlLabel.id = "volumeControlLabel";
 volumeControlLabel.classList.add("input-group-text");
 volumeControlLabel.innerHTML = "Volumen";

 let volumeControl = document.createElement("input");
 volumeControl.type = "range";
 volumeControl.min = 0;
 volumeControl.max = 1;
 volumeControl.step = 0.01;
 volumeControl.id = "volumeControl";
 volumeControl.style.height = "10px"; // Ajusta la altura aquí también

 volumeControl.addEventListener("input", function() {
  audioPlayer.volume = this.value;
 });

 volumeControlContainer.appendChild(volumeControlLabel);
 volumeControlContainer.appendChild(volumeControl);

 audioPlayer.parentNode.insertBefore(volumeControlContainer, audioPlayer.nextSibling);
});
