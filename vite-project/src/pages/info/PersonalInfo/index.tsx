// components/PersonalInfo.tsx
import React from "react";

interface InfoItem {
  icon: React.ReactNode;
  label: string;
}

interface PersonalInfoProps {
  info: InfoItem[];
}

const PersonalInfo: React.FC<PersonalInfoProps> = ({ info }) => {
  return (
    <div className="d-flex flex-column gap-16px pb-24 border-bottom-gray">
      {info.map((item, index) => (
        <div key={index} className="d-flex align-center gap-16px fs-16 text-color">
          {item.icon}
          <p>{item.label}</p>
        </div>
      ))}
    </div>
  );
};

export default PersonalInfo;
