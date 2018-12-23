<%@ page import="it.unibo.sdls.sampleproject.dao.*" %>
<%@ page import="java.util.*" %>

<%!
String printTableRow(Book book, String url) {
	StringBuffer html = new StringBuffer();
	html
		.append("<tr>")
		.append("<td>")
		.append(book.getTitle())
		.append("</td>")
		.append("<td>")
		.append(book.getIsbn10())
		.append("</td>")
		.append("<td>")
		.append(book.getIsbn13())
		.append("</td>")
		.append("<td>")
		.append( (book.getPublisher() == null) ? "n.d." : book.getPublisher().getName() )
		.append("</td>");
	String[] authors = { "n.d.", "n.d.", "n.d." };
	if ( book.getAuthors() != null ) {
		Iterator iterator = book.getAuthors().iterator();
		for ( int i = 0; i < 3 && iterator.hasNext() ; i++ ) {
			authors[i] = ((Author) iterator.next()).getName();
		}
	}
	for ( int i = 0; i < 3 ; i++ ) {
		html
			.append("<td>")
			.append( authors[i] )
			.append("</td>");
	}
	html
		.append("<td>")
		.append("<a href=\""+url+"?operation=deleteBook&id="+book.getId()+"\">delete</a>")
		.append("</td>")
		.append("</tr>");
	return html.toString();
}

String printTableRows(List books, String url) {
	StringBuffer html = new StringBuffer();
	Iterator iterator = books.iterator();
	while ( iterator.hasNext() ) {
		html.append( printTableRow( (Book) iterator.next(), url ) );
	}
	return html.toString();
}
%>

<%
// can't use builtin object 'application' while in a declaration! 
// must be in a scriptlet or expression!
DAOFactory daoFactory = DAOFactory.getDAOFactory( application.getInitParameter("dao") ); 
PublisherDAO publisherDAO = daoFactory.getPublisherDAO();
AuthorDAO authorDAO = daoFactory.getAuthorDAO();
BookDAO bookDAO = daoFactory.getBookDAO();

String operation = request.getParameter("operation");
if ( operation != null && operation.equals("insertPublisher") ) {
	Publisher publisher = new Publisher();
	publisher.setName( request.getParameter("name") );
	int id = publisherDAO.insertPublisher( publisher );
	out.println("<!-- inserted publisher '" + publisher.getName() + "', with id = '" + id + "' -->");
}
else if ( operation != null && operation.equals("insertAuthor") ) {
	Author author = new Author();
	author.setName( request.getParameter("name") );
	int id = authorDAO.insertAuthor( author );
	out.println("<!-- inserted author '" + author.getName() + "', with id = '" + id + "' -->");
}
else if ( operation != null && operation.equals("addBook") ) {
	Book book = new Book();
	book.setTitle( request.getParameter("title") );
	book.setIsbn10( request.getParameter("isbn10") );
	book.setIsbn13( request.getParameter("isbn13") );
	Publisher publisher = publisherDAO.findPublisherById( Integer.parseInt(request.getParameter("publisher")) );
	book.setPublisher( publisher );
	if ( book.getAuthors() == null ) book.setAuthors( new HashSet() );
	book.getAuthors().add( authorDAO.findAuthorById( Integer.parseInt(request.getParameter("author1")) ) );
	if ( ! request.getParameter("author2").equals("-1") ) 
		book.getAuthors().add( authorDAO.findAuthorById( Integer.parseInt(request.getParameter("author2")) ) );
	if ( ! request.getParameter("author3").equals("-1") ) 
		book.getAuthors().add( authorDAO.findAuthorById( Integer.parseInt(request.getParameter("author3")) ) );
	int id = bookDAO.addBook( book );
	out.println("<!-- added book '" + book.getTitle() + "' with id = '" + id + "' -->");
}
else if ( operation != null && operation.equals("deleteBook") ) {
	Book book = bookDAO.getBookById( Integer.parseInt( request.getParameter("id") ) );
	bookDAO.deleteBook( book.getId() );
	out.println("<!-- deleted book '" + book.getTitle() + "' with id = '" + book.getId() + "' -->");
}
%>

<html>

	<head>
		<title>Book Manager</title>
	
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="Mon, 01 Jan 1996 23:59:59 GMT"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<meta name="Author" content="you">

		<link rel="StyleSheet" href="styles/default.css" type="text/css" media="all" />
	
	</head>
	
	<body>
		<h1>Book Manager</h1>
		
		<div>
			<p>Add publisher:</p>
			<form>
				Name: <input type="text" name="name"/><br/>
				<input type="hidden" name="operation" value="insertPublisher"/>
				<input type="submit" name="submit" value="submit"/>
			</form>
		</div>

		<div>
			<p>Add author:</p>
			<form>
				Name: <input type="text" name="name"/><br/>
				<input type="hidden" name="operation" value="insertAuthor"/>
				<input type="submit" name="submit" value="submit"/>
			</form>
		</div>
<%
		List publishers = publisherDAO.findAllPublishers();
		List authors = authorDAO.findAllAuthors();
		if ( publishers.size() > 0 && authors.size() > 0 ) {
%>
			<div>
				<p>Add book:</p>
				<form>
					Title: <input type="text" name="title"/><br/>
					ISBN10: <input type="text" name="isbn10"/><br/>
					ISBN13: <input type="text" name="isbn13"/><br/>
					Publisher: <select name="publisher">
<%
						Iterator iterator = publishers.iterator();
						while ( iterator.hasNext() ) {
							Publisher publisher = (Publisher) iterator.next();
%>
							<option value="<%= publisher.getId() %>"><%= publisher.getName()%></option>
<%
						}// end while
%>
					</select><br/>
					Author #1: <select name="author1">
<%
						iterator = authors.iterator();
						while ( iterator.hasNext() ) {
							Author author = (Author) iterator.next();
%>
							<option value="<%= author.getId() %>"><%= author.getName()%></option>
<%
						}// end while
%>
					</select><br/>
					Author #2: <select name="author2">
<%
						iterator = authors.iterator();
						while ( iterator.hasNext() ) {
							Author author = (Author) iterator.next();
%>
							<option value="<%= author.getId() %>"><%= author.getName()%></option>
<%
						}// end while
%>
						<option value="-1" selected="selected">-</option>
					</select><br/>
					Author #3: <select name="author3">
<%
						iterator = authors.iterator();
						while ( iterator.hasNext() ) {
							Author author = (Author) iterator.next();
%>
							<option value="<%= author.getId() %>"><%= author.getName()%></option>
<%
						}// end while
%>
						<option value="-1" selected="selected">-</option>
				</select><br/>
				<input type="hidden" name="operation" value="addBook"/>
				<input type="submit" name="submit" value="submit"/>
			</form>
		</div>
<%
}// end if
else {
%>
		<div>
			<p>At least one publisher and one author must be present to add a new book.</p>
		</div>
<%
} // end else
%>
		<div>
			<p>Books currently in the database:</p>
			<table>
				<tr><th>Title</th><th>ISBN10</th><th>ISBN13</th><th>Publisher</th><th>Author #1</th><th>Author #2</th><th>Author #3</th><th></th></tr>
				<%= printTableRows( bookDAO.getAllBooksByAuthor(null), request.getContextPath() ) %>
			</table>
		</div>
		
		<div>
			<a href="<%= request.getContextPath() %>">Ricarica lo stato iniziale di questa pagina</a>
		</div>
		
	</body>

</html>
