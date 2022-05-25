package com.houcem.books.controllers;

import java.text.ParseException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.houcem.books.entities.Author;
import com.houcem.books.entities.Book;
import com.houcem.books.service.AuthorService;
import com.houcem.books.service.BookService;

@Controller
public class BookController {
@Autowired
BookService bookService;
@Autowired
AuthorService authService;
@RequestMapping("/showCreate")
public String showCreate(ModelMap modelMap)
{
List<Author> auths = authService.findAll();
Book prod = new Book();
Author a = new Author();
a = auths.get(0); // prendre la première catégorie de la liste
prod.setAuthor(a); //affedter une catégorie par défaut au Book pour éviter le pb avec une catégorie null
modelMap.addAttribute("authors", auths);
modelMap.addAttribute("book", new Book());
modelMap.addAttribute("mode", "new");
return "formBook";
}
@RequestMapping("/saveBook")
public String saveBook(@Valid Book book,
BindingResult bindingResult)
{
System.out.println(book);
if (bindingResult.hasErrors()) return "formBook";
bookService.saveBook(book);

return "redirect:/listBooks";
}

@RequestMapping("/showCreateAuth")
public String showCreateAuth(ModelMap modelMap)
{
modelMap.addAttribute("author", new Author());
modelMap.addAttribute("mode", "new");
return "formAuthor";
}
@RequestMapping("/saveAuthor")
public String saveAuthor(@ModelAttribute("Author") Author Author,ModelMap modelMap) throws ParseException 
{
authService.saveAuthor(Author);
return "redirect:/listAuth";
}

@RequestMapping("/listAuth")
public String listeAuth(ModelMap modelMap)
{
List <Author> auths = authService.findAll();
modelMap.addAttribute("authors", auths);
return "listAuth";
}

@RequestMapping("/listBooks")
public String listBooks(ModelMap modelMap,
@RequestParam (name="page",defaultValue = "0") int page,
@RequestParam (name="size", defaultValue = "4") int size)
{
Page<Book> books = bookService.getAllBookParPage(page, size);
List<Author> auths=authService.findAll();
modelMap.addAttribute("books", books);
modelMap.addAttribute("authors",auths);
 modelMap.addAttribute("pages", new int[books.getTotalPages()]);
modelMap.addAttribute("currentPage", page);
modelMap.addAttribute("size", size);
modelMap.addAttribute("mode", "all");
return "listBooks";
}

@RequestMapping("/search")
public String recherNom(@RequestParam("title") String title,
ModelMap modelMap)
{
List<Author> auths = authService.findAll();
modelMap.addAttribute("authors", auths);
List<Book> books =bookService.findByTitleBook(title);
modelMap.addAttribute("books",books);
modelMap.addAttribute("mode", "SearchTitle");
modelMap.addAttribute("title",title);
return "listSearch";
}
@RequestMapping("/searchAuth")
public String recherGrp(@RequestParam("author") Long auth,ModelMap modelMap)
{
Author a = new Author();
a.setIdAuth(auth);
List<Author> auths = authService.findAll();
modelMap.addAttribute("authors", auths);
List<Book> books = bookService.findByAuthor(a);
modelMap.addAttribute("books",books);
modelMap.addAttribute("mode", "SearchAuth");

return "listSearch";
}
@RequestMapping("/deleteBook")
public String deleteBook(@RequestParam("id") Long id,
ModelMap modelMap,
@RequestParam (name="page",defaultValue = "0") int page,
@RequestParam (name="size", defaultValue = "4") int size)
{
bookService.deleteBooktById(id);
Page<Book> books = bookService.getAllBookParPage(page, 
size);
modelMap.addAttribute("books", books);
modelMap.addAttribute("pages", new int[books.getTotalPages()]);
modelMap.addAttribute("currentPage", page);
modelMap.addAttribute("size", size);
return "listBooks";
}
@RequestMapping("/deleteAuthor")
public String deleteAuthor(@RequestParam("id") Long id,
 ModelMap modelMap)
{ 
authService.deleteAuthorById(id);
List<Author> auths = authService.findAll();
modelMap.addAttribute("authors", auths);
return "redirect:/listAuth";
}

@RequestMapping("/editBook")
public String editBook(@RequestParam("id") Long id,ModelMap modelMap)
{
 Book B= bookService.getBook(id);
List<Author> auths = authService.findAll();
auths.remove(B.getAuthor());
modelMap.addAttribute("authors", auths);
modelMap.addAttribute("book", B);
modelMap.addAttribute("mode", "edit");
return "formBook";
}
@RequestMapping("/updateBook")
public String updateBook(@ModelAttribute("Book") Book Book,ModelMap modelMap) throws ParseException {
	//conversion de la date 
	/* SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	 Date dateCreation = dateformat.parse(String.valueOf(date));
	 Book.setDateC(dateCreation);
	 */
	 
	bookService.updateBook(Book);
	 List<Book> books = bookService.getAllBooks();
	 modelMap.addAttribute("books", books);
	return "listBooks";
	}




@RequestMapping("/editAuthor")
public String editAuthor(@RequestParam("id") Long id,ModelMap modelMap)
{
Author a= authService.getAuthor(id);
modelMap.addAttribute("author", a);
modelMap.addAttribute("mode", "edit");
return "formAuthor";
}
@RequestMapping("/updateAuthor")
public String updateAuthor(@ModelAttribute("Author") Author Author,ModelMap modelMap) throws ParseException {
	authService.updateAuthor(Author);
	List<Author> auths = authService.findAll();
	modelMap.addAttribute("authors", auths);
	return "listAuth";
	}
}