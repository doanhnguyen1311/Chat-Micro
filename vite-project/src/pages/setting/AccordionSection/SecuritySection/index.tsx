import { useState } from "react";

const SecuritySection = () => {
  const [showSecurityNotification, setShowSecurityNotification] = useState(true);

  return (
    <div className="p-16 text-sm text-color-secondary fs-13">
      <div className="d-flex justify-between align-center">
        <p className="fw-semibold text-black">Show security notification</p>
        <input
          type="checkbox"
          checked={showSecurityNotification}
          onChange={() => setShowSecurityNotification(!showSecurityNotification)}
        />
      </div>
    </div>
  );
};

export default SecuritySection;
