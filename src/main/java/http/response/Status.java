package http.response;

public enum Status {
	NUMBER200(200), NUMBER302(302);
	
	int value;
	Status(int num){
		this.value = num;
	}
}
