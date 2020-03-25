
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="save" method="post">
	modelId<input name="modelId" value="${modelId}" /><br/>
	name<input name="name" value="${name}" /><br/>
	description<input name="description" /><br/>
	json_xml<textarea name="json_xml" cols="100" rows="60"  >${json_xml}</textarea><br/>
		svg_xml<input name="svg_xml" /><br/>
	<input type="submit" />
</form>

</body>
</html>