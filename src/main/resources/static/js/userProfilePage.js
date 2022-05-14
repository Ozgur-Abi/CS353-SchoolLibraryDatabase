function openEvents() {
    var events = document.getElementById('events');
	var memberships = document.getElementById('memberships');

	events.style.display = 'block';
	memberships.style.display = 'none';
}

function openMemberships() {
	var events = document.getElementById('events');
	var memberships = document.getElementById('memberships');

	events.style.display = 'none';
	memberships.style.display = 'block';
}