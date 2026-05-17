public class PasswordValidation {
    public static class Result{

        protected final Boolean valid;
        protected final String text;

        public Result(Boolean valid, String text) {
            this.valid = valid;
            this.text = text;
        }
    }
    
    public static Result valid(String password){
        
        if (password == null || password.length() < 8)
            return new Result(false, "the password can't be less than 8 characters!");
        else if (!password.matches(".*[0-9].*"))
            return new Result(false, "password must contain at least one number!");
        else if (!password.matches(".*[A-Z].*"))
            return new Result(false, "password must contain at least one upper case character!");
        else if (!password.matches(".*[a-z].*"))
            return new Result(false, "password must contain at least one lower case character!");
        else if (password.matches(".*\\s.*"))
            return new Result(false, "the password can't contain a space!");

        return new Result(true,"");

    }
    
}
