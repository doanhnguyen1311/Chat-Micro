import React from "react";
import SocialIcons from "./SocialIcon";

const SignUpForm: React.FC = () => {
  return (
    <div className="form-container sign-up">
      <form>
        <h1>Create Account</h1>
        <SocialIcons />
        <span>or use your email for registration</span>
        <input type="text" placeholder="Name" />
        <input type="email" placeholder="Email" />
        <input type="password" placeholder="Password" />
        <button>Sign Up</button>
      </form>
    </div>
  );
};

export default SignUpForm;
