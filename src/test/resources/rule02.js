function rule02(context) { 
	console.log('JS-input: ' + JSON.stringify(context));
	
	// rule begin
	
	if (context.tre=="3") {
		context.risultato = "OK";
	} else {
		context.risultato = "KO";
	}

	// rule end

	console.log('JS-output: ' + JSON.stringify(context));
	
	return context;
}