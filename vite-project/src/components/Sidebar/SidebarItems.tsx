import type React from "react";
import { useNavigate } from "react-router-dom";
import './css/SidebarItems.css';

interface SidebarItemProps {
  icon: React.ElementType;
  active?: boolean;
  path: string;
}

const SidebarItem: React.FC<SidebarItemProps> = ({ icon: Icon, active, path }) => {
    const navigate = useNavigate();
  return (
    <div
      className={
        `menu-nav relative p-3 rounded-md d-flex align-center justify-center cursor-pointer ${active ? "text-green" : "text-gray"}`
      }
      onClick={() => navigate(path)}
    >
      <Icon className="w-5 h-5" />
    </div>
  );
};

export default SidebarItem;
