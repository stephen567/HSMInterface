# HSMInterface

It intends to help quickly try and test HSM(Thales) commands using this program with an HSM Simulator or HSM itself. 

To setup an HSM Simulator refer http://www.unpluggedmind.in/index.php/2018/10/27/hsm-thales-simulator/
(Prefer to run the docker image)

Update /src/main/java/in/unpluggedmind/HSMInterface/HsmInterfaceInitialization.java for following and execute:
* HSM Simulator's IP and Port.
* Uncomment the command you'd like to try under Process method.
* Review console logs for the HSM Response.
