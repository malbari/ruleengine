function rule01(context) { 
	console.log('JS-input: ' + JSON.stringify(context));
	
	// rule begin
	
	if (context.uno=="1") {
		context.tre = "3";
		context.quattro = "4";		
	} else {
		context.tre = "4";
		context.quattro = "3";		
	}

	// rule end

	console.log('JS-output: ' + JSON.stringify(context));
	
	return context;
}