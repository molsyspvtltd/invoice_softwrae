
// import React, { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import axios from "axios";
// import Modal from "react-modal";
// import "./invoice.css";

// Modal.setAppElement("#root");

// const InvoiceApp = () => {
//   const [page, setPage] = useState(1);
//   const [selectedOption, setSelectedOption] = useState("");
//   const [designation, setDesignation] = useState("");
//   const [invoiceRows, setInvoiceRows] = useState([
//     { description: "", quantity: "", price: "" },
//   ]);
//   const [invoiceData, setInvoiceData] = useState({
//     date: "",
//     reference: "",
//     recipient: "",
//     gstinRecipient: "",
//     netTotal: "",
//     gst: "",
//     grandTotal: "",
//     amountWords: "",
//     gstin: "",
//     sac: "",
//   });

//   const [pdfUrl, setPdfUrl] = useState("");
//   const [modalIsOpen, setModalIsOpen] = useState(false);

//   const navigate = useNavigate();

//   const handleLogin = (e) => {
//     e.preventDefault();
//     if (designation) {
//       setPage(2);
//     } else {
//       alert("Please select your designation.");
//     }
//   };

//   const handleDropdownChange = (e) => {
//     setSelectedOption(e.target.value);
//   };

//   const handleDesignationChange = (e) => {
//     setDesignation(e.target.value);
//   };

//   const handleSubmit = () => {
//     if (selectedOption) {
//       if (selectedOption === "tax-invoice") {
//         setPage(3);
//       } else if (selectedOption === "form") {
//         navigate("/form"); // Navigate to Form.js page
//       }
//     } else {
//       alert("Please select an option.");
//     }
//   };

  

//   const handleInvoiceRowChange = (index, e) => {
//     const updatedRows = [...invoiceRows];
//     updatedRows[index][e.target.name] = e.target.value;
//     setInvoiceRows(updatedRows);
//   };

//   const addRow = () => {
//     setInvoiceRows([...invoiceRows, { description: "", quantity: "", price: "" }]);
//   };



  

//   const handleGeneratePDF = async () => {
//     try {
//       const response = await axios.post("http://localhost:5000/generate-pdf", { ...invoiceData, items: invoiceRows }, { responseType: "blob" });

//       const pdfBlob = new Blob([response.data], { type: "application/pdf" });
//       const pdfUrl = URL.createObjectURL(pdfBlob);
//       setPdfUrl(pdfUrl);
//       setModalIsOpen(true);
//     } catch (error) {
//       console.error("Error generating PDF:", error);
//     }
//   };

//   return (
//     <div className="container">
//       {page === 1 && (
//         <div className="login-box">
//           <h2>Login</h2>
//           <form onSubmit={handleLogin}>
//             <input type="email" placeholder="Email" required />
//             <input type="password" placeholder="Password" required />
//             <select value={designation} onChange={handleDesignationChange} required>
//               <option value="">Select Designation</option>
//               <option value="Admin">Admin</option>
//               <option value="VP Strategy">VP Strategy</option>
//               <option value="Manager">Manager</option>
//               <option value="IT">IT</option>
//               <option value="Analysis Team">Analysis Team</option>
//               <option value="Genomic Team">Genomic Team</option>
//             </select>
//             <button type="submit">Enter</button>
//           </form>
//         </div>
//       )}

//       {page === 2 && (
//         <div className="home">
//           <h1>Welcome</h1>
//           <div className="dropdown-container">
//             <select value={selectedOption} onChange={handleDropdownChange}>
//               <option value="">Select an option</option>
//               <option value="tax-invoice">Tax Invoice</option>
//               <option value="quotation">Quotation</option>
//               <option value="purchase-order">Purchase Order</option>
//             </select>
//             <button onClick={handleSubmit}>Submit</button>
//           </div>
//         </div>
//       )}

//       {page === 3 && selectedOption === "tax-invoice" && (
//         <div className="invoice-container">
//           <h2 className="invoice-title">TAX INVOICE</h2>

//           <div className="invoice-header">
//             <div>
//               <label>Dated:</label>
//               <input type="date" name="date" value={invoiceData.date} onChange={(e) => setInvoiceData({ ...invoiceData, date: e.target.value })} />
//             </div>
//             <div>
//               <label>Ref:</label>
//               <input type="text" name="reference" value={invoiceData.reference} onChange={(e) => setInvoiceData({ ...invoiceData, reference: e.target.value })} />
//             </div>
//           </div>

//           <div className="invoice-recipient">
//             <p>To,</p>
//             <textarea name="recipient" value={invoiceData.recipient} onChange={(e) => setInvoiceData({ ...invoiceData, recipient: e.target.value })} placeholder="Recipient details..." />
//             <div>
//               <label>GSTIN:</label>
//               <input type="text" name="gstinRecipient" value={invoiceData.gstinRecipient} onChange={(e) => setInvoiceData({ ...invoiceData, gstinRecipient: e.target.value })} />
//             </div>
//           </div>

//           <table className="invoice-table">
//             <thead>
//               <tr>
//                 <th>Description Of Services</th>
//                 <th>Qty</th>
//                 <th>Price (INR) Per Sample</th>
//               </tr>
//             </thead>
//             <tbody>
//               {invoiceRows.map((row, index) => (
//                 <tr key={index}>
//                   <td>
//                     <input type="text" name="description" value={row.description} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Enter description..." />
//                   </td>
//                   <td>
//                     <input type="number" name="quantity" value={row.quantity} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Qty" />
//                   </td>
//                   <td>
//                     <input type="text" name="price" value={row.price} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Price" />
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>

//           <button onClick={addRow}>+ Add Row</button>

//           <div className="invoice-summary">
//             <div>
//               <label>Net Total:</label>
//               <input type="text" name="netTotal" value={invoiceData.netTotal} onChange={(e) => setInvoiceData({ ...invoiceData, netTotal: e.target.value })} />
//             </div>
//             <div>
//               <label>GST @ 18%:</label>
//               <input type="text" name="gst" value={invoiceData.gst} onChange={(e) => setInvoiceData({ ...invoiceData, gst: e.target.value })} />
//             </div>
//             <div>
//               <label>Grand Total (Round Off):</label>
//               <input type="text" name="grandTotal" value={invoiceData.grandTotal} onChange={(e) => setInvoiceData({ ...invoiceData, grandTotal: e.target.value })} />
//             </div>
//           </div>

//           <button onClick={handleGeneratePDF}>Generate PDF</button>
//           <button onClick={() => setPage(2)}>Back to Home</button>
//           <button onClick={() => window.location.href = "/"}>Exit</button>
//           <button onClick={() => alert("Logging out...")}>Logout</button>
//         </div>
//       )}
    


    
//     </div>
//   );
// };

// export default InvoiceApp;



// import React, { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import axios from "axios";
// import Modal from "react-modal";
// import "./invoice.css";

// Modal.setAppElement("#root");

// const InvoiceApp = () => {
//   const [page, setPage] = useState(1);
//   const [selectedOption, setSelectedOption] = useState("");
//   const [designation, setDesignation] = useState("");
//   const [invoiceRows, setInvoiceRows] = useState([{ description: "", quantity: "", price: "" }]);
//   const [invoiceData, setInvoiceData] = useState({
//     date: "",
//     reference: "",
//     recipient: "",
//     gstinRecipient: "",
//     netTotal: "",
//     gst: "",
//     grandTotal: "",
//     amountWords: "",
//     gstin: "",
//     sac: "",
//   });

//   const [purchaseOrderData, setPurchaseOrderData] = useState({
//     date: "",
//     reference: "",
//     recipient: "",
//     items: [{ itemName: "", quantity: "", unitPrice: "" }],
//   });

//   const [qty, setQty] = useState(6);
//   const [pdfUrl, setPdfUrl] = useState("");
//   const [modalIsOpen, setModalIsOpen] = useState(false);

//   const navigate = useNavigate();

//   const handleLogin = (e) => {
//     e.preventDefault();
//     if (designation) {
//       setPage(2);
//     } else {
//       alert("Please select your designation.");
//     }
//   };

//   const handleDropdownChange = (e) => {
//     setSelectedOption(e.target.value);
//   };

//   const handleDesignationChange = (e) => {
//     setDesignation(e.target.value);
//   };

//   const handleSubmit = () => {
//     if (selectedOption) {
//       setPage(selectedOption === "tax-invoice" ? 3 : selectedOption === "quotation" ? 4 : 5);
//     } else {
//       alert("Please select an option.");
//     }
//   };

//   const handleInvoiceRowChange = (index, e) => {
//     const updatedRows = [...invoiceRows];
//     updatedRows[index][e.target.name] = e.target.value;
//     setInvoiceRows(updatedRows);
//   };

//   const addRow = () => {
//     setInvoiceRows([...invoiceRows, { description: "", quantity: "", price: "" }]);
//   };

//   const handleGeneratePDF = async () => {
//     try {
//       const response = await axios.post("http://localhost:5000/generate-pdf", { ...invoiceData, items: invoiceRows }, { responseType: "blob" });

//       const pdfBlob = new Blob([response.data], { type: "application/pdf" });
//       setPdfUrl(URL.createObjectURL(pdfBlob));
//       setModalIsOpen(true);
//     } catch (error) {
//       console.error("Error generating PDF:", error);
//     }
//   };

//   const handlePurchaseOrderChange = (index, e) => {
//     const updatedItems = [...purchaseOrderData.items];
//     updatedItems[index][e.target.name] = e.target.value;
//     setPurchaseOrderData({ ...purchaseOrderData, items: updatedItems });
//   };

//   const addPurchaseOrderRow = () => {
//     setPurchaseOrderData({ ...purchaseOrderData, items: [...purchaseOrderData.items, { itemName: "", quantity: "", unitPrice: "" }] });
//   };

//   const handleGeneratePO_PDF = async () => {
//     try {
//       const response = await axios.post("http://localhost:5000/generate-po-pdf", purchaseOrderData, { responseType: "blob" });

//       const pdfBlob = new Blob([response.data], { type: "application/pdf" });
//       setPdfUrl(URL.createObjectURL(pdfBlob));
//       setModalIsOpen(true);
//     } catch (error) {
//       console.error("Error generating Purchase Order PDF:", error);
//     }
//   };

//   return (
//     <div className="container">
//       {page === 1 && (
//         <div className="login-box">
//           <h2>Login</h2>
//           <form onSubmit={handleLogin}>
//             <input type="email" placeholder="Email" required />
//             <input type="password" placeholder="Password" required />
//             <select value={designation} onChange={handleDesignationChange} required>
//               <option value="">Select Designation</option>
//               <option value="Admin">Admin</option>
//               <option value="VP Strategy">VP Strategy</option>
//               <option value="Manager">Manager</option>
//               <option value="IT">IT</option>
//               <option value="Analysis Team">Analysis Team</option>
//               <option value="Genomic Team">Genomic Team</option>
//             </select>
//             <button type="submit">Enter</button>
//           </form>
//         </div>
//       )}

//       {page === 2 && (
//         <div className="home">
//           <h1>Welcome</h1>
//           <div className="dropdown-container">
//             <select value={selectedOption} onChange={handleDropdownChange}>
//               <option value="">Select an option</option>
//               <option value="tax-invoice">Tax Invoice</option>
//               <option value="quotation">Quotation</option>
//               <option value="purchase-order">Purchase Order</option>
//             </select>
//             <button onClick={handleSubmit}>Submit</button>
//           </div>
//         </div>
//       )}

// {page === 3 && selectedOption === "tax-invoice" && (
//         <div className="invoice-container">
//           <h2 className="invoice-title">TAX INVOICE</h2>

//           <div className="invoice-header">
//             <div>
//               <label>Dated:</label>
//               <input type="date" name="date" value={invoiceData.date} onChange={(e) => setInvoiceData({ ...invoiceData, date: e.target.value })} />
//             </div>
//             <div>
//               <label>Ref:</label>
//               <input type="text" name="reference" value={invoiceData.reference} onChange={(e) => setInvoiceData({ ...invoiceData, reference: e.target.value })} />
//             </div>
//           </div>

//           <div className="invoice-recipient">
//             <p>To,</p>
//             <textarea name="recipient" value={invoiceData.recipient} onChange={(e) => setInvoiceData({ ...invoiceData, recipient: e.target.value })} placeholder="Recipient details..." />
//             <div>
//               <label>GSTIN:</label>
//               <input type="text" name="gstinRecipient" value={invoiceData.gstinRecipient} onChange={(e) => setInvoiceData({ ...invoiceData, gstinRecipient: e.target.value })} />
//             </div>
//           </div>

//           <table className="invoice-table">
//             <thead>
//               <tr>
//                 <th>Description Of Services</th>
//                 <th>Qty</th>
//                 <th>Price (INR) Per Sample</th>
//               </tr>
//             </thead>
//             <tbody>
//               {invoiceRows.map((row, index) => (
//                 <tr key={index}>
//                   <td>
//                     <input type="text" name="description" value={row.description} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Enter description..." />
//                   </td>
//                   <td>
//                     <input type="number" name="quantity" value={row.quantity} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Qty" />
//                   </td>
//                   <td>
//                     <input type="text" name="price" value={row.price} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Price" />
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>

//           <button onClick={addRow}>+ Add Row</button>

//           <div className="invoice-summary">
//             <div>
//               <label>Net Total:</label>
//               <input type="text" name="netTotal" value={invoiceData.netTotal} onChange={(e) => setInvoiceData({ ...invoiceData, netTotal: e.target.value })} />
//             </div>
//             <div>
//               <label>GST @ 18%:</label>
//               <input type="text" name="gst" value={invoiceData.gst} onChange={(e) => setInvoiceData({ ...invoiceData, gst: e.target.value })} />
//             </div>
//             <div>
//               <label>Grand Total (Round Off):</label>
//               <input type="text" name="grandTotal" value={invoiceData.grandTotal} onChange={(e) => setInvoiceData({ ...invoiceData, grandTotal: e.target.value })} />
//             </div>
//           </div>

//           <button onClick={handleGeneratePDF}>Generate PDF</button>
//           <button onClick={() => setPage(2)}>Back to Home</button>
//           <button onClick={() => window.location.href = "/"}>Exit</button>
//           <button onClick={() => alert("Logging out...")}>Logout</button>
//         </div>
//       )}
    

//       {page === 4 && (
//         <div className="quotation-container">
//           <h2>QUOTATION FOR TRANSCRIPTOME SEQUENCING</h2>
//           <div className="header">
//             <p><strong>Dated:</strong> <input type="date" /></p>
//             <p><strong>Ref:</strong> <input type="text" placeholder="Ref No" /></p>
//           </div>

//           <div className="recipient">
//             <p><strong>To,</strong></p>
//             <textarea placeholder="Recipient Details" rows="4"></textarea>
//           </div>

//           <table className="quotation-table">
//             <thead>
//               <tr>
//                 <th>Cat No.</th>
//                 <th>Description of Services</th>
//                 <th>Qty</th>
//                 <th>Price (INR) Per Sample</th>
//               </tr>
//             </thead>
//             <tbody>
//               <tr>
//                 <td><input type="text" defaultValue="MoIS_TS_1 7_NovaS eq6000" /></td>
//                 <td>
//                   <strong>Transcriptome Sequencing:</strong>
//                   <ul>
//                     <li><strong>Sample Processing</strong> - RNA QC using gel electrophoresis, fluorometer, etc.</li>
//                     <li><strong>Library Preparation</strong> - Short Insert Library Preparation, DNA HS Assay Kit.</li>
//                     <li><strong>Sequencing</strong> - 25-30M reads, 150X2 chemistry, >85% Q30 FASTQ data.</li>
//                     <li><strong>Deliverables</strong> - Raw data with QC.</li>
//                   </ul>
//                 </td>
//                 <td><input type="number" value={qty} onChange={(e) => setQty(e.target.value)} /></td>
//                 <td><input type="text" defaultValue="13,500.00" /></td>
//               </tr>
//             </tbody>
//           </table>

//           <button className="submit-btn" onClick={() => alert("Quotation Submitted!")}>Submit</button>
//           <button onClick={() => setPage(2)}>Back</button>
//         </div>
//       )}


// {page === 5 && selectedOption === "purchase-order" && (
//   <div className="purchase-order-container">
//     <h2 className="purchase-order-title">Purchase Order</h2>

//     <div className="purchase-order-header">
//       <div>
//         <label>Dated:</label>
//         <input type="date" value={purchaseOrderData.date} onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, date: e.target.value })} />
//       </div>
//       <div>
//         <label>PO No:</label>
//         <input type="text" value={purchaseOrderData.poNumber} onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, poNumber: e.target.value })} />
//       </div>
//     </div>

//     <div className="purchase-order-recipient">
//       <p>To,</p>
//       <textarea value={purchaseOrderData.recipient} onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, recipient: e.target.value })} placeholder="Recipient details..." />
//     </div>

//     <table className="purchase-order-table">
//       <thead>
//         <tr>
//           <th>Sl No</th>
//           <th>Project</th>
//           <th>Service</th>
//           <th>Quantity</th>
//           <th>Unit Price (INR)</th>
//           <th>Cost (INR)</th>
//           <th>Action</th> 
//         </tr>
//       </thead>
//       <tbody>
//         {purchaseOrderData.items.map((item, index) => (
//           <tr key={index}>
//             <td>{index + 1}</td>
//             <td>
//               <input type="text" name="project" value={item.project} onChange={(e) => handlePurchaseOrderChange(index, e)} placeholder="Project Name" />
//             </td>
//             <td>
//               <input type="text" name="service" value={item.service} onChange={(e) => handlePurchaseOrderChange(index, e)} placeholder="Service Details" />
//             </td>
//             <td>
//               <input type="number" name="quantity" value={item.quantity} onChange={(e) => handlePurchaseOrderChange(index, e)} placeholder="Qty" />
//             </td>
//             <td>
//               <input type="text" name="unitPrice" value={item.unitPrice} onChange={(e) => handlePurchaseOrderChange(index, e)} placeholder="Unit Price" />
//             </td>
//             <td>
//               <input type="text" name="cost" value={(item.quantity * item.unitPrice).toFixed(2)} readOnly />
//             </td>
//             <td>
//               <button onClick={() => removePurchaseOrderRow(index)}>-</button>
//             </td>
//           </tr>
//         ))}
//       </tbody>
//     </table>

//     <button onClick={addPurchaseOrderRow}>+ Add Row</button>

//     <div className="purchase-order-summary">
//       <p>Net Total: <span>{purchaseOrderData.netTotal} INR</span></p>
//       <p>Taxes (18%): <span>{purchaseOrderData.taxAmount} INR</span></p>
//       <p><strong>Grand Total (Round Off): <span>{purchaseOrderData.grandTotal} INR</span></strong></p>
//       <p>Amount (Words): <span>{purchaseOrderData.amountWords}</span></p>
//     </div>

//     <div className="purchase-order-footer">
//       <label>HSN Code:</label>
//       <input type="text" value={purchaseOrderData.hsnCode} onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, hsnCode: e.target.value })} />

//       <label>GSTIN:</label>
//       <input type="text" value={purchaseOrderData.gstin} onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, gstin: e.target.value })} />
//     </div>

//     {/* View PDF Popup */}
//     {showPDF && (
//       <div className="pdf-popup">
//         <h3>Purchase Order PDF</h3>
//         <iframe src={pdfURL} width="100%" height="500px"></iframe>
//         <button onClick={() => setShowPDF(false)}>Close</button>
//       </div>
//     )}

//     <div className="purchase-order-buttons">
//       <button onClick={handleGeneratePO_PDF}>Generate PDF</button>
//       <button onClick={() => setShowPDF(true)}>View PDF</button>
//       <button onClick={() => setPage(2)}>Back</button>
//       <button onClick={handleExit}>Exit</button>
//       <button onClick={handleLogout}>Logout</button>
//     </div>
//   </div>
// )}

//     </div>
//   );
// };

// export default InvoiceApp;











import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Modal from "react-modal";
import "./invoice.css";

Modal.setAppElement("#root");

const InvoiceApp = () => {
  const [page, setPage] = useState(1);
  const [selectedOption, setSelectedOption] = useState("");
  const [designation, setDesignation] = useState("");
  const [invoiceRows, setInvoiceRows] = useState([
    { description: "", quantity: "", price: "" }
  ]);

  const [invoiceData, setInvoiceData] = useState({
    date: "",
    reference: "",
    recipient: "",
    gstinRecipient: "",
    netTotal: "",
    gst: "",
    grandTotal: "",
    amountWords: "",
    gstin: "",
    sac: "",
  });

  const [purchaseOrderData, setPurchaseOrderData] = useState({
    date: "",
    poNumber: "",
    recipient: "",
    hsnCode: "",
    gstin: "",
    netTotal: 0,
    taxAmount: 0,
    grandTotal: 0,
    amountWords: "",
    items: [{ project: "", service: "", quantity: "", unitPrice: "" }],
  });
  const [recipients, setRecipients] = useState([""]);
  const [quotationRows, setQuotationRows] = useState([
    { catNo: "", description: "", qty: "", price: "" }
  ]);
  
  const [pdfURL, setPdfURL] = useState("");
  const [showPDF, setShowPDF] = useState(false);
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    if (designation) {
      setPage(2);
    } else {
      alert("Please select your designation.");
    }
  };

  const handleDropdownChange = (e) => setSelectedOption(e.target.value);
  const handleDesignationChange = (e) => setDesignation(e.target.value);

  const handleSubmit = () => {
    if (selectedOption) {
      setPage(selectedOption === "tax-invoice" ? 3 : selectedOption === "quotation" ? 4 : 5);
    } else {
      alert("Please select an option.");
    }
  };

  const [password, setPassword] = useState("");
  const [passwordStrength, setPasswordStrength] = useState("");
  
  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
  
    // Password strength logic
    if (value.length >= 1 && value.length <= 4) {
      setPasswordStrength("Poor");
    } else if (value.length >= 5 && value.length <= 8) {
      setPasswordStrength("Fair");
    } else if (
      value.length > 8 && 
      /[A-Z]/.test(value) &&  // At least one uppercase
      /[!@#$%^&*]/.test(value) // At least one special character
    ) {
      setPasswordStrength("Strong");
    } else {
      setPasswordStrength("Fair");
    }
  };
  

  // ✅ Fixed: Corrected `handleInvoiceRowChange`
  const handleInvoiceRowChange = (index, event) => {
    const { name, value } = event.target;
    const updatedRows = invoiceRows.map((row, i) =>
      i === index ? { ...row, [name]: value } : row
    );
    setInvoiceRows(updatedRows);
  };

  // ✅ Fixed: Added correct row structure
  const addRow = () => {
    setInvoiceRows([...invoiceRows, { description: "", quantity: "", price: "" }]);
  };

  // ✅ Fixed: Handle PDF generation properly
  const handleGeneratePDF = () => {
    console.log("Generate PDF logic will go here.");
  };

  const handlePurchaseOrderChange = (index, e) => {
    const updatedItems = [...purchaseOrderData.items];
    updatedItems[index][e.target.name] = e.target.value;
    setPurchaseOrderData({ ...purchaseOrderData, items: updatedItems });
    calculateTotals(updatedItems);
  };

  const addPurchaseOrderRow = () => {
    setPurchaseOrderData({
      ...purchaseOrderData,
      items: [...purchaseOrderData.items, { project: "", service: "", quantity: "", unitPrice: "" }],
    });
  };

  const removePurchaseOrderRow = (index) => {
    const updatedItems = purchaseOrderData.items.filter((_, i) => i !== index);
    setPurchaseOrderData({ ...purchaseOrderData, items: updatedItems });
    calculateTotals(updatedItems);
  };

  const calculateTotals = (items) => {
    let netTotal = items.reduce((acc, item) => acc + ((item.quantity || 0) * (item.unitPrice || 0)), 0);
    let taxAmount = (netTotal * 0.18).toFixed(2);
    let grandTotal = (parseFloat(netTotal) + parseFloat(taxAmount)).toFixed(2);

    setPurchaseOrderData((prev) => ({
      ...prev,
      netTotal,
      taxAmount,
      grandTotal,
      amountWords: convertToWords(grandTotal),
    }));
  };

  const convertToWords = (num) => {
    return num ? `${num} INR` : "";
  };
// Handle Recipient Change
const handleRecipientChange = (index, event) => {
  const updatedRecipients = [...recipients];
  updatedRecipients[index] = event.target.value;
  setRecipients(updatedRecipients);
};

// Add New Recipient
const addRecipient = () => {
  setRecipients([...recipients, ""]);
};

// Remove Recipient
const removeRecipient = (index) => {
  setRecipients(recipients.filter((_, i) => i !== index));
};
const removeRow = (index) => {
  setInvoiceRows(invoiceRows.filter((_, i) => i !== index));
};
// Handle Quotation Row Change
const handleQuotationRowChange = (index, field, value) => {
  const updatedRows = [...quotationRows];
  updatedRows[index][field] = value;
  setQuotationRows(updatedRows);
};
const [pdfUrl, setPdfUrl] = useState("");

// Generate PDF from Backend
const generatePDF = async () => {
  try {
    const response = await fetch("http://your-backend-url/generate-pdf", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ recipients, quotationRows }),
    });

    if (response.ok) {
      const data = await response.json();
      setPdfUrl(data.pdfUrl); // Set PDF URL to view in popup
    } else {
      alert("Failed to generate PDF");
    }
  } catch (error) {
    console.error("Error generating PDF:", error);
  }
};

// Logout & Navigate Back
const logout = () => {
  alert("Logging out...");
  setPage(2);
};

// Add New Quotation Row
const addQuotationRow = () => {
  setQuotationRows([...quotationRows, { catNo: "", description: "", qty: "", price: "" }]);
};

// Remove Quotation Row
const removeQuotationRow = (index) => {
  setQuotationRows(quotationRows.filter((_, i) => i !== index));
};

  const handleGeneratePO_PDF = async () => {
    try {
      const response = await axios.post("http://localhost:5000/generate-po-pdf", purchaseOrderData, { responseType: "blob" });
      const pdfBlob = new Blob([response.data], { type: "application/pdf" });
      setPdfURL(URL.createObjectURL(pdfBlob));
      setShowPDF(true);
    } catch (error) {
      console.error("Error generating Purchase Order PDF:", error);
    }
  };

  const handleExit = () => {
    alert("Exiting the application...");
  };

  const handleLogout = () => {
    setPage(1); // Redirect to Login Page
  
  };
  return (
    <div className="container">
{page === 1 && (
  <div className="login-box">
    <h2>Login</h2>
    <form onSubmit={handleLogin}>
      <input type="email" placeholder="Email" required />
      
      <input 
        type="password" 
        placeholder="Password" 
        required 
        value={password}
        onChange={handlePasswordChange} 
      />
      <p className={`password-strength ${passwordStrength}`}>{passwordStrength}</p>

      <select value={designation} onChange={handleDesignationChange} required>
        <option value="">Select Designation</option>
        <option value="Admin">Admin</option>
        <option value="VP Strategy">VP Strategy</option>
        <option value="Manager">Manager</option>
        <option value="IT">IT</option>
        <option value="Analysis Team">Analysis Team</option>
        <option value="Genomic Team">Genomic Team</option>
      </select>

      <button type="submit">Enter</button>
    </form>
  </div>
)}


  {page === 2 && (
    <div className="home">
      <h1>Welcome</h1>
      <div className="dropdown-container">
        <select value={selectedOption} onChange={handleDropdownChange}>
          <option value="">Select an option</option>
          <option value="tax-invoice">Tax Invoice</option>
          <option value="quotation">Quotation</option>
          <option value="purchase-order">Purchase Order</option>
        </select>
        <button onClick={handleSubmit}>Submit</button>
      </div>
    </div>
  )}

{page === 3 && selectedOption === "tax-invoice" && (
  <div className="invoice-container">
    <h2 className="invoice-title">TAX INVOICE</h2>

    <div className="invoice-header">
      <div>
        <label>Dated:</label>
        <input type="date" name="date" value={invoiceData.date} onChange={(e) => setInvoiceData({ ...invoiceData, date: e.target.value })} />
      </div>
      <div>
        <label>Ref:</label>
        <input type="text" name="reference" value={invoiceData.reference} onChange={(e) => setInvoiceData({ ...invoiceData, reference: e.target.value })} />
      </div>
    </div>

    <div className="invoice-recipient">
      <p>To,</p>
      <textarea name="recipient" value={invoiceData.recipient} onChange={(e) => setInvoiceData({ ...invoiceData, recipient: e.target.value })} placeholder="Recipient details..." />
      <div>
        <label>GSTIN:</label>
        <input type="text" name="gstinRecipient" value={invoiceData.gstinRecipient} onChange={(e) => setInvoiceData({ ...invoiceData, gstinRecipient: e.target.value })} />
      </div>
    </div>

    <table className="invoice-table">
      <thead>
        <tr>
          <th>Description Of Services</th>
          <th>Qty</th>
          <th>Price (INR) Per Sample</th>
          <th>Remove</th>
        </tr>
      </thead>
      <tbody>
        {invoiceRows.map((row, index) => (
          <tr key={index}>
            <td>
              <input type="text" name="description" value={row.description} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Enter description..." />
            </td>
            <td>
              <input type="number" name="quantity" value={row.quantity} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Qty" />
            </td>
            <td>
              <input type="text" name="price" value={row.price} onChange={(e) => handleInvoiceRowChange(index, e)} placeholder="Price" />
            </td>
            <td>
              <button onClick={() => removeRow(index)}>-</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>

    <button onClick={addRow}>+ Add Row</button>

    <div className="invoice-summary">
      <div>
        <label>Net Total:</label>
        <input type="text" name="netTotal" value={invoiceData.netTotal} onChange={(e) => setInvoiceData({ ...invoiceData, netTotal: e.target.value })} />
      </div>
      <div>
        <label>GST @ 18%:</label>
        <input type="text" name="gst" value={invoiceData.gst} onChange={(e) => setInvoiceData({ ...invoiceData, gst: e.target.value })} />
      </div>
      <div>
        <label>Grand Total (Round Off):</label>
        <input type="text" name="grandTotal" value={invoiceData.grandTotal} onChange={(e) => setInvoiceData({ ...invoiceData, grandTotal: e.target.value })} />
      </div>
    </div>

    <button onClick={handleGeneratePDF}>Generate PDF</button>
    <button onClick={() => setPage(2)}>Back to Home</button>
    <button onClick={() => setPage(1)}>Logout</button>
  </div>
)}


{page === 4 && (
  <div className="quotation-container">
    <h2>QUOTATION FOR TRANSCRIPTOME SEQUENCING</h2>

    {/* Header Section */}
    <div className="header">
      <p><strong>Dated:</strong> <input type="date" /></p>
      <p><strong>Ref:</strong> <input type="text" placeholder="Ref No" /></p>
    </div>

    {/* Recipient Details (Multiple Recipients Allowed) */}
    <div className="recipient">
      <p><strong>To:</strong></p>
      {recipients.map((recipient, index) => (
        <div key={index} className="recipient-box">
          <textarea 
            placeholder="Enter Recipient Details" 
            rows="3" 
            value={recipient}
            onChange={(e) => handleRecipientChange(index, e)}
          />
          <button onClick={() => removeRecipient(index)}>❌</button>
        </div>
      ))}
      <button onClick={addRecipient}>➕ Add Recipient</button>
    </div>
    <div className="quotation-gst">
      <label>GST:</label>
      <input 
        type="text" 
        value={purchaseOrderData.gstin} 
        onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, gstin: e.target.value })} 
      />
    </div>
    {/* Quotation Table */}
    <table className="quotation-table">
      <thead>
        <tr>
          <th>Cat No.</th>
          <th>Description of Services</th>
          <th>Qty</th>
          <th>Price (INR) Per Sample</th>
        </tr>
      </thead>
      <tbody>
        {quotationRows.map((row, index) => (
          <tr key={index}>
            <td>
              <input 
                type="text" 
                value={row.catNo} 
                onChange={(e) => handleQuotationRowChange(index, "catNo", e.target.value)} 
                placeholder="Enter Cat No." 
              />
            </td>
            <td>
              <textarea 
                rows="4" 
                value={row.description} 
                onChange={(e) => handleQuotationRowChange(index, "description", e.target.value)} 
                placeholder="Enter Description"
              />
            </td>
            <td>
              <input 
                type="number" 
                value={row.qty} 
                onChange={(e) => handleQuotationRowChange(index, "qty", e.target.value)} 
                placeholder="Qty" 
              />
            </td>
            <td>
              <input 
                type="text" 
                value={row.price} 
                onChange={(e) => handleQuotationRowChange(index, "price", e.target.value)} 
                placeholder="Price (INR)" 
              />
            </td>
            <td>
              <button onClick={() => removeQuotationRow(index)}>❌</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>

    {/* Buttons */}
    <button onClick={addQuotationRow}>➕ Add Row</button>
    <button className="submit-btn" onClick={generatePDF}>Generate PDF</button>
    <button onClick={() => setPage(2)}>Back to Home</button>
    <button onClick={() => setPage(1)}>Logout</button>

    {/* View PDF Popup */}
    {pdfUrl && (
      <div className="popup">
        <div className="popup-content">
          <span className="close-btn" onClick={() => setPdfUrl("")}>❌</span>
          <iframe src={pdfUrl} width="100%" height="500px"></iframe>
        </div>
      </div>
    )}
  </div>
)}




{page === 5 && selectedOption === "purchase-order" && (
  <div className="purchase-order-container">
    <h2 className="purchase-order-title">Purchase Order</h2>

    <div className="purchase-order-header">
      <div>
        <label>Dated:</label>
        <input 
          type="date" 
          value={purchaseOrderData.date} 
          onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, date: e.target.value })} 
        />
      </div>
      <div>
        <label>PO No:</label>
        <input 
          type="text" 
          value={purchaseOrderData.poNumber} 
          onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, poNumber: e.target.value })} 
        />
      </div>
    </div>

    <div className="purchase-order-recipient">
      <p>To,</p>
      <textarea 
        value={purchaseOrderData.recipient} 
        onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, recipient: e.target.value })} 
        placeholder="Recipient details..." 
      />
    </div>

    <div className="purchase-order-gst">
      <label>GST:</label>
      <input 
        type="text" 
        value={purchaseOrderData.gstin} 
        onChange={(e) => setPurchaseOrderData({ ...purchaseOrderData, gstin: e.target.value })} 
      />
    </div>

    <table className="purchase-order-table">
      <thead>
        <tr>
          <th>Sl No</th>
          <th>Project</th>
          <th>Service</th>
          <th>Quantity</th>
          <th>Unit Price (INR)</th>
          <th>Cost (INR)</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {purchaseOrderData.items.map((item, index) => (
          <tr key={index}>
            <td>{index + 1}</td>
            <td>
              <input 
                type="text" 
                name="project" 
                value={item.project} 
                onChange={(e) => handlePurchaseOrderChange(index, e)} 
              />
            </td>
            <td>
              <input 
                type="text" 
                name="service" 
                value={item.service} 
                onChange={(e) => handlePurchaseOrderChange(index, e)} 
              />
            </td>
            <td>
              <input 
                type="number" 
                name="quantity" 
                value={item.quantity} 
                onChange={(e) => handlePurchaseOrderChange(index, e)} 
              />
            </td>
            <td>
              <input 
                type="text" 
                name="unitPrice" 
                value={item.unitPrice} 
                onChange={(e) => handlePurchaseOrderChange(index, e)} 
              />
            </td>
            <td>
              <input type="text" value={(item.quantity * item.unitPrice).toFixed(2)} readOnly />
            </td>
            <td>
              <button onClick={() => removePurchaseOrderRow(index)}>-</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>

    <button onClick={addPurchaseOrderRow}>+ Add Row</button>

    <div className="purchase-order-summary">
      <p>Net Total: <span>{purchaseOrderData.netTotal} INR</span></p>
      <p>Taxes (18%): <span>{purchaseOrderData.taxAmount} INR</span></p>
      <p><strong>Grand Total: <span>{purchaseOrderData.grandTotal} INR</span></strong></p>
    </div>

    {showPDF && (
      <div className="pdf-popup">
        <h3>Purchase Order PDF</h3>
        <iframe src={pdfURL} width="100%" height="500px" title="Purchase Order PDF"></iframe>
        <button onClick={() => setShowPDF(false)}>Close</button>
      </div>
    )}

    <div className="purchase-order-buttons">
      <button onClick={handleGeneratePO_PDF}>Generate PDF</button>
      <button onClick={() => setShowPDF(true)}>View PDF</button>
      <button onClick={() => setPage(2)}>Back</button>
      <button onClick={handleExit}>Exit</button>
      <button onClick={handleLogout}>Logout</button>
    </div>
  </div>
)}


</div>
);
};

export default InvoiceApp;