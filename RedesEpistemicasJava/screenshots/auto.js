try{
	var s;
	var autoPlay = function(){
		if(document.location.href.indexOf("#autoplay")!=-1){
			s = document.location.href.split("/");
			s = s[s.length-1];
			s = s.split("\\");
			s = s[s.length-1];
			s = s.split(".");
			s = s[0];
			s = parseInt(s.substring(1))+1;

			if(!isNaN(s)){
				autoPlay=function(){}
				setTimeout("goNext("+s+")",1500);
			}
		}
	}
	setInterval(autoPlay, 500);
	var goNext = function(s){
		document.location.href = "p" + s +".html#autoplay";
	}
	
}catch(e){

}