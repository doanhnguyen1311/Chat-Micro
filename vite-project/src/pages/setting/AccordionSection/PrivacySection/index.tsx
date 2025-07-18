import { useState } from "react";

const PrivacySection = () => {
  const [privacy, setPrivacy] = useState({
    profilePhoto: "Selected",
    lastSeen: true,
    status: "Everyone",
    readReceipts: true,
    groups: "Everyone",
  });

  return (
    <div className="d-flex flex-column text-sm text-color-secondary fs-13">
      <div className="d-flex justify-between align-center py-16 mx-16 border-bottom-gray">
        <p className="fw-semibold text-black">Profile photo</p>
        <select
          value={privacy.profilePhoto}
          onChange={(e) =>
            setPrivacy({ ...privacy, profilePhoto: e.target.value })
          }
          className="border p-4px radius-4px"
        >
          <option value="Everyone">Everyone</option>
          <option value="Selected">Selected</option>
          <option value="Nobody">Nobody</option>
        </select>
      </div>

      <div className="d-flex justify-between align-center border-top py-16 mx-16 border-bottom-gray">
        <p className="fw-semibold text-black">Last seen</p>
        <input
          type="checkbox"
          checked={privacy.lastSeen}
          onChange={() =>
            setPrivacy({ ...privacy, lastSeen: !privacy.lastSeen })
          }
        />
      </div>

      <div className="d-flex flex-column border-top py-16 mx-16 border-bottom-gray">
        <div className="d-flex justify-between align-start">
          <p className="fw-semibold text-black">Status</p>
          <select
            value={privacy.status}
            onChange={(e) =>
              setPrivacy({ ...privacy, status: e.target.value })
            }
          >
            <option value="Everyone">Everyone</option>
            <option value="Contacts">Selected</option>
            <option value="Nobody">Nobody</option>
          </select>
        </div>
        <p className="mt-8 fs-16 text-color">displayStatus</p>
      </div>

      <div className="d-flex justify-between align-center py-16 mx-16 border-bottom-gray">
        <p className="fw-semibold text-black">Read receipts</p>
        <input
          type="checkbox"
          checked={privacy.readReceipts}
          onChange={() =>
            setPrivacy({ ...privacy, readReceipts: !privacy.readReceipts })
          }
        />
      </div>

      <div className="d-flex justify-between align-center py-16 mx-16">
        <p className="fw-semibold text-black">Groups</p>
        <select
          value={privacy.groups}
          onChange={(e) =>
            setPrivacy({ ...privacy, groups: e.target.value })
          }
          className="border p-4px radius-4px"
        >
          <option value="Everyone">Everyone</option>
          <option value="Contacts">Selected</option>
          <option value="Nobody">Nobody</option>
        </select>
      </div>
    </div>
  );
};

export default PrivacySection;
