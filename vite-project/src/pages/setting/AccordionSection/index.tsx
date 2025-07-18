import { useState } from "react";
import {
  User,
  Lock,
  Shield,
  HelpCircle,
  ChevronDown,
} from "lucide-react";
import PersonalInfoSection from "./PersonalInfoSection";
import PrivacySection from "./PrivacySection";
import SecuritySection from "./SecuritySection";
import HelpSection from "./HelpSection";

const AccordionSection = () => {
  const [openIndex, setOpenIndex] = useState<number | null>();
  const toggle = (index: number) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  const sections = [
    { title: "Personal Info", icon: <User size={16} />, content: <PersonalInfoSection /> },
    { title: "Privacy", icon: <Lock size={16} />, content: <PrivacySection /> },
    { title: "Security", icon: <Shield size={16} />, content: <SecuritySection /> },
    { title: "Help", icon: <HelpCircle size={16} />, content: <HelpSection /> },
  ];

  return (
    <div className="border radius-8px overflow-hidden">
      {sections.map((item, index) => (
        <div key={index} className="border-b">
          <div
            className="d-flex justify-between align-center cursor-pointer p-16 border-bottom-gray"
            onClick={() => toggle(index)}
          >
            <div className="d-flex align-center gap-8px fw-medium text-color fs-14">
              {item.icon}
              {item.title}
            </div>
            <ChevronDown
              size={16}
              className={`transition-transform duration-200 ${
                openIndex === index ? "rotate-180" : "rotate-0"
              }`}
            />
          </div>
          {openIndex === index && (
            <div className="bg-white border-bottom-gray">{item.content}</div>
          )}
        </div>
      ))}
    </div>
  );
};

export default AccordionSection;
