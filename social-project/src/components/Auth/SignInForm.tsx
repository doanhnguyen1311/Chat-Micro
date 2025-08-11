import React, { useState } from "react";
import SocialIcons from "./SocialIcon";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const SignInForm: React.FC = () => {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    // const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    //     e.preventDefault();

    //     try {
    //         const userData = await login(email, password);
    //         console.log('userData', userData);
            
    //         if (userData) {
    //             alert("Login successful!");
    //             navigate('/feeds');
    //         }

    //     } catch (error) {
    //         console.error("Login error:", error);
    //         if (error instanceof Error) {
    //             alert(`Login failed: ${error.message}`);
    //         } else {
    //             alert("Login failed: Unknown error");
    //         }
    //     }
    // }

    return (
        <div className="form-container sign-in">
            {/* onSubmit={handleSubmit} */}
            <form>
                <h1>Sign In</h1>
                <SocialIcons />
                <span>or use your email password</span>
                <input 
                    type="text" 
                    placeholder="Email" 
                    value={email}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
                    required
                />
                <input 
                    type="password" 
                    placeholder="Password" 
                    value={password}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)}
                    required
                />
                <a href="#">Forget Your Password?</a>
                <Link to={'feeds'}><button type="submit">Sign In</button></Link>
                {/* <button type="submit">Sign In</button> */}
            </form>
        </div>
    );
};

export default SignInForm;
