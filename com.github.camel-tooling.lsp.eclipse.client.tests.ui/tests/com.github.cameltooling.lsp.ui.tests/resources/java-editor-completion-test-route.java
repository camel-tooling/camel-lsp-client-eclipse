import src.main.java.com.mycompany.camel.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {
    public void configure() {
        from("file:work/cbr/input")
        	.choice()
        		.when(xpath("/person/city = 'London'"))
        			.to("file:target/messages/uk")
        		.otherwise()
        			.to("file:target/messages/others");
    }
}