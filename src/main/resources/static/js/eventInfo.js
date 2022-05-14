function displayDescription() {
    let description = document.getElementById('description');
    let participants = document.getElementById('participants');

    participants.style.display = 'none';
    description.style.display = 'block';
};
function displayParticipants() {
    let description = document.getElementById('description');
    let participants = document.getElementById('participants');

    description.style.display = 'none';
    participants.style.display = 'block';
};