import { EllipsisVertical, ChevronDown } from "lucide-react";
import avatar from '../../assets/imgs/avatar-default.jpg';
import bg_info from '../../assets/imgs/bg-info.jpg';
import { useEffect, useRef, useState } from "react";
import AccordionSection from "./AccordionSection";
import './index.css';

type StatusType = {
    label: string;
    color: string;
    className: string;
};
const statuses: StatusType[] = [
    { label: "Active", color: "green", className: "dot-green" },
    { label: "Away", color: "yellow", className: "dot-yellow" },
    { label: "Do not disturb", color: "red", className: "dot-red" },
];

const Setting = () => {
    const [status, setStatus] = useState<StatusType>(statuses[0]);
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

    const handleStatusChange = (newStatus: StatusType) => {
        setStatus(newStatus);
        setShowDropdown(false);
    }

    return (
        <div className="d-flex flex-column">
            <div className="sticky top-0 bg-white index-1">
                <div className="user-profile-img relative">
                    <img src={bg_info} alt="bg" className="bg-info-img"/>
                    <div className="overlay-content">
                        <div className="d-flex justify-between align-center p-16 text-white">
                            <h1 className="fs-20 fw-medium">Settings</h1>
                            <EllipsisVertical size={20} className="cursor-pointer"/>
                        </div>
                    </div>
                </div>
                <div className="d-flex flex-column align-center info-container">
                    <img src={avatar} alt="avatar" className="avatar-lg radius-50 mb-16"/>
                    <p className="mb-8 fs-16 fw-medium text-color">Tin Nguyen</p>
                    <div className="relative">
                        <div 
                            className="fs-14 fw-medium text-color-secondary d-flex align-center gap-4px cursor-pointer"
                            onClick={toggleDropdown}
                        >
                            <span className={status.className}></span>
                            {status.label}
                            <ChevronDown size={16}/>
                        </div>
                        {showDropdown && (
                            <div 
                                ref={dropdownRef}
                                className="dropdown-menu absolute bg-white left-0 top-100per box-shadow p-16 mt-4 radius-8px"
                            >
                                {statuses.map((item, index) => (
                                    <div
                                        key={index}
                                        className="d-flex align-center gap-8px cursor-pointer fs-14 fw-medium text-color-secondary"
                                        onClick={() => handleStatusChange(item)}
                                    >
                                        <span className={item.className}></span>
                                        {item.label}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <div className="d-flex flex-column">
                <AccordionSection />
            </div>
        </div>
    )
}

export default Setting;