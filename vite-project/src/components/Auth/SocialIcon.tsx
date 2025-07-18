import React from "react";
import { FaGooglePlusG, FaFacebookF, FaGithub, FaLinkedinIn } from "react-icons/fa";

const SocialIcons: React.FC = () => {
  return (
    <div className="social-icons">
      <a href="#" className="icon">
        <FaGooglePlusG />
      </a>
      <a href="#" className="icon">
        <FaFacebookF />
      </a>
      <a href="#" className="icon">
        <FaGithub />
      </a>
      <a href="#" className="icon">
        <FaLinkedinIn />
      </a>
    </div>
  );
};

export default SocialIcons;
