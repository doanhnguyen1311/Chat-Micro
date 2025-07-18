import SidebarItem from "./SidebarItems";
import { sidebarItems } from "./SidebarConfig";
import avatar from '../../assets/imgs/avatar-default.jpg';
import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import {
  UserCircle,
  Settings,
  Lock,
  LogOut,
} from "lucide-react";
import './css/SidebarItems.css';

const Sidebar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
          dropdownRef.current &&
          !dropdownRef.current.contains(event.target as Node)
      ) {
          setShowDropdown(false);
      }
    };
    
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
        document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const toggleDropdown = () => setShowDropdown(prev => !prev);

  const menuItems = [
    { label: "Profile", icon: <UserCircle size={16} />, path: "/info" },
    { label: "Setting", icon: <Settings size={16} />, path: "/settings" },
    { label: "Change Password", icon: <Lock size={16} />, path: "/change-password" },
    { label: "Log out", icon: <LogOut size={16} />, path: "/" },
  ];

  return (
    <div className="d-flex flex-column justify-between min-w-75px max-w-75px min-h-570px bg-primary py-24 relative">
      <div className="d-flex flex-column gap-52px">
        {sidebarItems.map((item, index) => (
          <SidebarItem
            key={index}
            icon={item.icon}
            path={item.path}
            active={location.pathname === item.path}
          />
        ))}
      </div>

      <div className="relative mx-auto">
        <div className="mx-auto cursor-pointer" onClick={toggleDropdown}>
          <img
            src={avatar}
            alt="Avatar"
            className="w-40 h-40 radius-50 border"
          />
        </div>
  
        {showDropdown && (
          <div 
            ref={dropdownRef}
            className="dropdown-profile-menu absolute bg-white bottom-100per box-shadow p-16 mb-8 radius-8"
          >
            {menuItems.map((item, index) => (
              <div
                key={index}
                className="d-flex align-center justify-between gap-8px cursor-pointer fs-14 fw-medium text-color-secondary radius-6px"
                onClick={() => {
                  navigate(item.path);
                  setShowDropdown(false);
                }}
              >
                <span>{item.label}</span>
                {item.icon}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Sidebar;
