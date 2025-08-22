import React, { useState } from "react";
import SocialIcons from "./SocialIcon";
import { Link, useNavigate } from "react-router-dom";

interface LoginResponse {
    data: {
        token: string | null;
        authentication: boolean;
    };
    message: string;
    statusCode: number;
    success: boolean;
}

const SignInForm: React.FC = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    // const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    //     e.preventDefault();

    //     try {
    //         const response = await fetch("https://auth.delichat.online/api/v1/auth/login", {
    //             method: "POST",
    //             headers: {
    //                 "Content-Type": "application/json"
    //             },
    //             body: JSON.stringify({
    //                 username: email,
    //                 password: password
    //             })
    //         });

    //         const data: LoginResponse = await response.json();
    //         console.log(data);
            

    //         if (!response.ok || !data.success || !data.data.authentication) {
    //             throw new Error(data.message || "Login failed");
    //         }

    //         if (data.success) {
    //             alert("Login successful!");
    //             navigate('/chat');
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
            <form >
                <h1>Sign In</h1>
                <SocialIcons />
                <span>or use your email password</span>
                <input 
                    type="email" 
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
                {/* <button type="submit">Sign In</button> */}
                <Link to={"chat"}>Sign In</Link>
            </form>
        </div>
    );
};

export default SignInForm;
