
function checkInput() {
	for (group = 1; group <= 5; group++) {	
		name = 'radio_' + group + '_'
		optionSelected = false
		
		for (i = 1; i <= 5; i++) {
			if (document.getElementById(name + i).checked) {
				optionSelected = true
			}
		}
		
		if (!optionSelected) {
			alert("Completati toate intrebarile inainte de a posta formularul")
			return false
		}
	}
	
	return true
}